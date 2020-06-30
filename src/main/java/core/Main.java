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
import core.commands.math.*;
import core.commands.music.*;

import core.listeners.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import utilities.*;
import utilities.math.Trigonometry;
import utilities.music.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  Main class of the EchoedDungeons project
 *
 * @author EchoedAJ
 * @since April 2020
 */
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
    private static final Trigonometry trig = new Trigonometry();
    private static final Logger log = new Logger();

    // LavaPlayer specific
    private static AudioPlayerManager audioManager;
    private static MusicUtilities musicUtils;
    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    /**
     * Main Method of the EchoedDungeons project.
     * Everything starts here. It is it's own patient zero.
     *
     * @param args args
     */
    public static void main(String[] args) {
        debugOnlyInitialization();

        preInitialization();
        initialization();
        postInitialization();
    }

    // ----- Initialization -----

    /**
     * Initialization run only when Debug is active.
     */
    private static void debugOnlyInitialization() {
        if (getConfig().getDebug()) {
            getLog().debug("Welcome to EchoedDungeons! \n \n", Constants.stagePreInit);
            getLog().debug("Prefix: " + getConfig().getPrefix(), Constants.stagePreInit);
            getLog().debug("Game Status: " + "config.getGameStatus()", Constants.stagePreInit);
            getLog().debug("Debug Status: " + "true", Constants.stagePreInit);
            getLog().debug("Token: " + getConfig().getToken(), Constants.stagePreInit);
        }
    }

    /**
     * Pre-Initialization
     * Sets start time, begins building the JDA instance
     */
    private static void preInitialization() {
        getLog().debug("Beginning Pre-Initialization.", Constants.stagePreInit);

        // Welcome User to the Bot.
        getLog().welcome();

        time = System.currentTimeMillis();

        builder = JDABuilder.createDefault(getConfig().getToken())
                .setAutoReconnect(true);
    }

    /**
     * Initialization
     * Defines the JDA instance, shard if applicable
     */
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

    /**
     * Post-Initialization
     * Set ID, register commands, start ChatterBot, start Audio Player
     */
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

    /**
     * Registers commands.
     */
    private static void registerCommands() {
        // Generic Commands
        getApi().addEventListener(help.registerCommand(help));
        getApi().addEventListener(help.registerCommand(new InfoCommand()));
        getApi().addEventListener(help.registerCommand(new PleaseHelpCommand()));

        // Math Commands
        getApi().addEventListener(help.registerCommand(new MathCommand()));
        getApi().addEventListener(help.registerCommand(new SetModeCommand()));

        // DND commands
        getApi().addEventListener(help.registerCommand(new RollCommand()));
        getApi().addEventListener(help.registerCommand(new CallCommand()));
        getApi().addEventListener(help.registerCommand(new FactCommand()));

        // DND Database commands
        getApi().addEventListener(help.registerCommand(new ClassCommand()));
        getApi().addEventListener(help.registerCommand(new FeatureCommand()));
        getApi().addEventListener(help.registerCommand(new LanguageCommand()));

        // Music commands
        getApi().addEventListener(help.registerCommand(new PlayCommand()));
        getApi().addEventListener(help.registerCommand(new SkipCommand()));
        getApi().addEventListener(help.registerCommand(new PauseCommand()));
        getApi().addEventListener(help.registerCommand(new QueueCommand()));
        getApi().addEventListener(help.registerCommand(new StopCommand()));
        getApi().addEventListener(help.registerCommand(new NowPlayingCommand()));
        getApi().addEventListener(help.registerCommand(new VolumeCommand()));

        // Admin commands
        getApi().addEventListener(help.registerCommand(new ShutdownCommand()));
    }

    /**
     * Initialize music player.
     */
    private static void initMusicPlayer() {
        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioPlayer player = audioManager.createPlayer();

        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicUtils = new MusicUtilities();
    }

    /**
     * Shuts down the bot.
     * @param status status under which the bot shut down
     */
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

    /**
     * Returns the API
     * @return the API
     */
    public static JDA getApi() {
        return api;
    }

    /**
     * Returns the bot's ID
     * @return the bot's ID
     */
    public static String getId() {
        return id;
    }

    /**
     * Returns the Logger instance
     * @return the Logger instance
     */
    public static Logger getLog() {
        return log;
    }

    /**
     * Returns the Configuration instance
     * @return the Configuration instance
     */
    public static Config getConfig() {
        return config;
    }

    /**
     * Returns the Music Utilities Instance
     * @return the Music Utilities Instance
     */
    public static MusicUtilities getMusicUtils() {
        return musicUtils;
    }

    /**
     * Returns the ChatterBot Session
     * @return the ChatterBot Session
     */
    public static ChatterBotSession getChatterBotSession() {
        return chatSession;
    }

    /**
     * Returns the Audio Player Manager
     * @return the Audio Player Manager
     */
    public static AudioPlayerManager getAudioManager() {
        return audioManager;
    }

    /**
     * Returns the Music Managers
     * @return the Music Managers
     */
    public static Map<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    /**
     * Returns the Trigonometry Instance
     * @return the Trigonometry Instance
     */
    public static Trigonometry getTrig() {
        return trig;
    }
}
