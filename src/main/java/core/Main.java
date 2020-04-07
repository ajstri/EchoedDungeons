/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
import core.commands.*;
import core.commands.admin.ShutdownCommand;
import core.commands.dnd.CallCommand;
import core.commands.dnd.FactCommand;
import core.commands.general.HelpCommand;
import core.commands.general.MathCommand;
import core.commands.general.PingCommand;
import core.commands.music.PauseCommand;
import core.commands.music.PlayCommand;
import core.commands.music.QueueCommand;
import core.commands.music.SkipCommand;
import core.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import utils.Constants;
import utils.Logger;
import utils.music.GuildMusicManager;
import utils.music.MusicUtils;
import utils.music.TrackScheduler;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    // jda specific
    public static JDA api;
    private static JDABuilder builder;
    public static String id;

    // bot specific
    public static final Config config = new Config();
    public static ChatterBotSession bot1session;
    public static final HelpCommand help = new HelpCommand();
    private static long time = System.currentTimeMillis();

    // LavaPlayer specific
    public static AudioPlayerManager audioManager;
    public static AudioPlayer player;
    public static TrackScheduler trackScheduler;
    public static MusicUtils utils;
    public static Map<Long, GuildMusicManager> musicManagers;

    public static void main(String[] args) {

        debugOnlyInitialization();

        preInitialization();
        initialization();
        postInitialization();

    }

    private static void preInitialization() {
        Logger.debug("Beginning Pre-Initialization.", Constants.stagePreInit);

        // Welcome User to the Bot.
        Logger.blank("", Constants.ANSI_PURPLE,
                "                                                     \n" +
                        "                     ,---._                                   \n" +
                        "   ,---,           .-- -.' \\                   ,----,        \n" +
                        "  '  .' \\          |    |   :       ,---.    .'   .' \\      \n" +
                        " /  ;    '.        :    :   |      /__./|  ,----,'    |       \n" +
                        ":  :       \\       :        | ,---.;  ; |  |    :  .  ;      \n" +
                        ":  |   /\\   \\      |    :   :/___/ \\  | |  :    |.'  /     \n" +
                        "|  :  ' ;.   :     :         \\   ;  \\ ' |  `----'/  ;       \n" +
                        "|  |  ;/  \\   \\    |    ;   | \\   \\  \\: |    /  ;  /     \n" +
                        "'  :  | \\  \\ ,'___ l           :   \\  ' .   ;  /  /-,      \n" +
                        "|  |  '  '--'/    /\\    J   :   \\   \\   '  /  /  /.`|      \n" +
                        "|  :  :     /  ../  `..-    ,    \\   `  ;./__;      :        \n" +
                        "|  | ,'     \\    \\         ;      :   \\ ||   :    .'       \n" +
                        "`--''        \\    \\      ,'        '---\" :   | .'          \n" +
                        "              \"---....--'                `---'               \n" +
                        "                                                              \n"
        );

        Logger.blank("", Constants.ANSI_CYAN, "E  C  H  O  _  C  O  R  E  --  V  E  R  S  I  O  N  " + Constants.VERSION);
        Logger.blank("", Constants.ANSI_YELLOW, "\t\t\t\tVersion: \t" + Constants.VERSION);
        Logger.blank("", Constants.ANSI_YELLOW, "\t\t\t\tBuild: \t\t" + Constants.BUILD_NUMBER);
        Logger.blank("", Constants.ANSI_YELLOW, "\t\t\t\tJVM: \t\t" + Constants.JVM + "\n");
        Logger.blank("", Constants.ANSI_WHITE, "\n\n\n");

        time = System.currentTimeMillis();

        builder = JDABuilder.createDefault(config.getToken())
                .setAutoReconnect(true);
                //.setActivity(Activity.watching("time pass by"));

    }

    private static void initialization() {
        Logger.debug("Beginning initialization.", Constants.stageInit);
        // Define the JDA Instance.
        try {
            Logger.debug("Defining JDA instance.", Constants.stageInit);
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
                api.awaitReady();
            //}
        }
        catch (LoginException le){
            Logger.error("Unable to define the JDA instance.", le);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (InterruptedException ie) {
            Logger.error("Interrupted upon waiting JDA Instance.", ie);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (ErrorResponseException ere) {
            Logger.error("Unable to connect.", ere);
            shutdown(Constants.STATUS_UNABLE_TO_CONNECT);
        }

        //registerCommands();
    }

    private static void postInitialization() {
        Logger.debug("Beginning post-initialization.", Constants.stagePostInit);

        // Set the Bot's ID.
        try {
            id = api.getSelfUser().getId();
            Logger.debug("Bot ID: " + id, Constants.stagePostInit);
        }
        catch (Exception e) {
            Logger.error("Error retrieving Bot ID. This is not a vital step, but may cause issues later.", e);
        }

        // Set auto-reconnect to true & set game status.
        api.setAutoReconnect(true);
        api.getPresence().setActivity(Activity.watching("time pass by"));

        registerCommands();

        // Initialize ChatterBot

        ChatterBot bot1;

        try {
            ChatterBotFactory factory = new ChatterBotFactory();
            bot1 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            bot1session = bot1.createSession();
        }
        catch (Exception e) {
            Logger.warning("Unable to initialize ChatterBot.");
        }

        // Initialize audio portion
        initMusicPlayer();
    }

    private static void debugOnlyInitialization() {
        if (config.getDebug()) {
            Logger.debug("Welcome to EchoedDungeons! \n \n", Constants.stagePreInit);
            Logger.debug("Prefix: " + config.getPrefix(), Constants.stagePreInit);
            Logger.debug("Game Status: " + "config.getGameStatus()", Constants.stagePreInit);
            Logger.debug("Debug Status: " + "true", Constants.stagePreInit);
            Logger.debug("Token: " + config.getToken(), Constants.stagePreInit);
        }
    }

    /**
     * Adds event listeners.
     */
    private static void registerEventListeners() {
        try {
            api.addEventListener(new TagListener());
        }
        catch (Exception e) {
            Logger.error("Unable to register Event Listeners.", e);
            shutdown(Constants.STATUS_NO_EVENT);
        }
    }

    private static void registerCommands() {
        // Non-DND commands
        api.addEventListener(help.registerCommand(help));
        api.addEventListener(help.registerCommand(new PingCommand()));
        api.addEventListener(help.registerCommand(new MathCommand()));

        // DND commands
        api.addEventListener(help.registerCommand(new RollCommand()));
        api.addEventListener(help.registerCommand(new CallCommand()));
        api.addEventListener(help.registerCommand(new FactCommand()));

        // Music commands
        api.addEventListener(help.registerCommand(new PlayCommand()));
        api.addEventListener(help.registerCommand(new SkipCommand()));
        api.addEventListener(help.registerCommand(new PauseCommand()));
        api.addEventListener(help.registerCommand(new QueueCommand()));

        // Admin commands
        api.addEventListener(new ShutdownCommand());
    }

    public static void shutdown(int status) {
        System.exit(status);

        long endTime = System.currentTimeMillis();
        long timeActive = endTime - time;

        Logger.info("Active for " + (timeActive / 1000) + " seconds");
        Logger.info("Beginning shutdown.");

        // Remove event listeners. The Bot can shutdown before these are defined.
        try {
            api.removeEventListener(api.getRegisteredListeners());
        }
        catch (NullPointerException npe) {
            Logger.debug("No Event Listeners to remove.", Constants.stageShutdown);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException ie) {
            Logger.debug("Ignored InterruptedException on shut down.", Constants.stageShutdown);
        }

        if (status != Constants.STATUS_NO_JDA && status != Constants.STATUS_CONFIG_UNUSABLE && status != Constants.STATUS_UNABLE_TO_CONNECT) {
            api.shutdownNow();
        }
        System.exit(status);
    }

    private static void initMusicPlayer() {
        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        player = audioManager.createPlayer();

        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicManagers = new HashMap<>();
        utils = new MusicUtils(musicManagers, Main.audioManager);

    }

}
