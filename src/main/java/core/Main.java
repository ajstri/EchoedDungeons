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

import core.commands.dnd.database.character.*;
import core.commands.dnd.database.other.FactCommand;
import core.commands.dnd.database.other.LanguageCommand;
import core.commands.dnd.utility.CallCommand;
import core.commands.dnd.utility.RollCommand;
import core.commands.general.InfoCommand;
import core.commands.math.MathCommand;
import echoedcore.core.EchoedCore;
import echoedcore.core.commands.Command;
import core.commands.admin.*;
import echoedcore.utilities.Logger;
import utilities.Config;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final EchoedCore main = new EchoedCore();
    private static final Logger botLogging = new Logger("EchoedDungeons");
    private static final Config config = new Config();

    public static void main(String[] args) {
        // Start the bot.
        main.registerConfiguration(config);
        main.startup();
        registerCommands();
        main.enableMusicCommands();
        main.enableInternalLogging();
    }

    private static void registerCommands() {
        // Register a set of commands
        List<Command> commandListAll = new ArrayList<>(){{
            // Admin
            add(new ShutdownCommand());
            add(new TestCommand());
            add(new UpdateConfigCommand());
            // Generic
            add(new InfoCommand());
            // Math
            add(new MathCommand());
            // D&D
            add(new BackgroundCommand());
            add(new ClassCommand());
            add(new FeatureCommand());
            add(new RaceCommand());
            add(new SubclassCommand());

            add(new FactCommand());
            add(new LanguageCommand());

            add(new CallCommand());
            add(new RollCommand());
        }};
        main.registerCommands(commandListAll);
    }

    public static EchoedCore getCore() {
        return main;
    }

    public static Config getConfig() {
        return config;
    }

    public static Logger getBotLogging() {
        return botLogging;
    }
}