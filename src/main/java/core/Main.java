/*
 *  Copyright 2020 EchoedAJ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package core;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import config.Config;

import core.commands.admin.*;
import core.commands.dnd.*;
import core.commands.general.*;
import core.commands.music.*;

import core.listeners.*;

import dndinfo.DatabaseManager;
import dndinfo.classes.*;
import dndinfo.other.features.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import utilities.*;
import utilities.music.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    // jda specific
    private static JDA api;
    private static JDABuilder builder;
    private static String id;

    // bot specific
    private static final Config config = new Config();
    private static ChatterBotSession chatSession;
    private static final HelpCommand help = new HelpCommand();
    private static long time = System.currentTimeMillis();

    // database - temporary
    private static final DatabaseManager database = new DatabaseManager();
    private static final Logger log = new Logger();

    // LavaPlayer specific
    private static AudioPlayerManager audioManager;
    private static MusicUtilities musicUtils;
    private static Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public static void main(String[] args) {
        debugOnlyInitialization();

        preInitialization();
        initialization();
        postInitialization();
    }

    // ----- Initialization -----

    private static void debugOnlyInitialization() {
        if (getConfig().getDebug()) {
            getLog().debug("Welcome to EchoedDungeons! \n \n", Constants.stagePreInit);
            getLog().debug("Prefix: " + getConfig().getPrefix(), Constants.stagePreInit);
            getLog().debug("Game Status: " + "config.getGameStatus()", Constants.stagePreInit);
            getLog().debug("Debug Status: " + "true", Constants.stagePreInit);
            getLog().debug("Token: " + getConfig().getToken(), Constants.stagePreInit);
        }
    }

    private static void preInitialization() {
        getLog().debug("Beginning Pre-Initialization.", Constants.stagePreInit);

        // Welcome User to the Bot.
        getLog().welcome();

        time = System.currentTimeMillis();

        builder = JDABuilder.createDefault(getConfig().getToken())
                .setAutoReconnect(true);
    }

    private static void initialization() {
        getLog().debug("Beginning initialization.", Constants.stageInit);
        // Define the JDA Instance.
        try {
            getLog().debug("Defining JDA instance.", Constants.stageInit);
            /*
            if (config.getShards() != 0) {
                // Adding event listeners.
                registerEventListeners();

                // Sharding.
                for (int i = 0; i < config.getShards(); i++)
                {
                    api = builder.useSharding(i, config.getShards())
                            .build();
                }
            }
            else { */
                api = builder.build();
                registerEventListeners();
                getApi().awaitReady();
            //}
        }
        catch (LoginException le){
            getLog().error("Unable to define the JDA instance.", le);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (InterruptedException ie) {
            getLog().error("Interrupted upon waiting JDA Instance.", ie);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (ErrorResponseException ere) {
            getLog().error("Unable to connect.", ere);
            shutdown(Constants.STATUS_UNABLE_TO_CONNECT);
        }

    }

    private static void postInitialization() {
        getLog().debug("Beginning post-initialization.", Constants.stagePostInit);

        // Set the Bot's ID.
        try {
            id = getApi().getSelfUser().getId();
            getLog().debug("Bot ID: " + getId(), Constants.stagePostInit);
        }
        catch (Exception e) {
            getLog().error("Error retrieving Bot ID. This is not a vital step, but may cause issues later.", e);
        }

        // Set auto-reconnect to true & set game status.
        getApi().setAutoReconnect(true);
        getApi().getPresence().setActivity(Activity.watching("time pass by"));

        registerCommands();

        // Initialize ChatterBot
        ChatterBot chatBot;

        try {
            ChatterBotFactory factory = new ChatterBotFactory();
            chatBot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            chatSession = chatBot.createSession();
        }
        catch (Exception e) {
            getLog().warning("Unable to initialize ChatterBot.");
        }

        // Initialize audio portion
        initMusicPlayer();
    }

    // ----- Registration Methods -----

    /**
     * Adds event listeners.
     */
    private static void registerEventListeners() {
        try {
            getApi().addEventListener(new TagListener());
        }
        catch (Exception e) {
            getLog().error("Unable to register Event Listeners.", e);
            shutdown(Constants.STATUS_NO_EVENT);
        }
    }

    private static void registerCommands() {
        // Register database
        registerDNDDatabase();

        // Non-DND commands
        getApi().addEventListener(help.registerCommand(help));
        getApi().addEventListener(help.registerCommand(new MathCommand()));
        getApi().addEventListener(help.registerCommand(new InfoCommand()));

        // DND commands
        getApi().addEventListener(help.registerCommand(new RollCommand()));
        getApi().addEventListener(help.registerCommand(new CallCommand()));
        getApi().addEventListener(help.registerCommand(new FactCommand()));

        // DND Database commands
        getApi().addEventListener(help.registerCommand(database.getClasses()));
        getApi().addEventListener(help.registerCommand(database.getFeatures()));
        getApi().addEventListener(help.registerCommand(database.getLanguages()));

        // Music commands
        getApi().addEventListener(help.registerCommand(new PlayCommand()));
        getApi().addEventListener(help.registerCommand(new SkipCommand()));
        getApi().addEventListener(help.registerCommand(new PauseCommand()));
        getApi().addEventListener(help.registerCommand(new QueueCommand()));
        getApi().addEventListener(help.registerCommand(new StopCommand()));
        getApi().addEventListener(help.registerCommand(new NowPlayingCommand()));

        // Admin commands
        getApi().addEventListener(new ShutdownCommand());
    }

    private static void registerDNDDatabase() {
        // Classes
        database.getClasses().registerClass(new Artificer());
        database.getClasses().registerClass(new Barbarian());
        database.getClasses().registerClass(new Bard());

        // Backgrounds


        // Races


        // Features
        new ArtificerFeatures(database.getFeatures());
        new BarbarianFeatures(database.getFeatures());
        //new BardFeatures(features);
    }

    private static void initMusicPlayer() {
        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioPlayer player = audioManager.createPlayer();

        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicUtils = new MusicUtilities();
    }

    public static void shutdown(int status) {
        System.exit(status);

        long endTime = System.currentTimeMillis();
        long timeActive = endTime - time;

        getLog().info("Active for " + (timeActive / 1000) + " seconds");
        getLog().info("Beginning shutdown.");

        // Remove event listeners. The Bot can shutdown before these are defined.
        try {
            getApi().removeEventListener(api.getRegisteredListeners());
        }
        catch (NullPointerException npe) {
            getLog().debug("No Event Listeners to remove.", Constants.stageShutdown);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException ie) {
            getLog().debug("Ignored InterruptedException on shut down.", Constants.stageShutdown);
        }

        if (status != Constants.STATUS_NO_JDA && status != Constants.STATUS_CONFIG_UNUSABLE && status != Constants.STATUS_UNABLE_TO_CONNECT) {
            getApi().shutdownNow();
        }
        System.exit(status);
    }

    // ----- Getter & Setter Methods -----

    public static JDA getApi() {
        return api;
    }

    public static String getId() {
        return id;
    }

    public static Logger getLog() {
        return log;
    }

    public static Config getConfig() {
        return config;
    }

    public static MusicUtilities getMusicUtils() {
        return musicUtils;
    }

    public static ChatterBotSession getChatterBotSession() {
        return chatSession;
    }

    public static AudioPlayerManager getAudioManager() {
        return audioManager;
    }

    public static Map<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }
}
