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
package core.commands.dnd.database.character;

import core.Main;
import echoedcore.core.EchoedCore;
import echoedcore.core.commands.Command;
import echoedcore.utilities.MessageUtilities;
import utilities.EchoedDungeonsConstants;
import utilities.dnd.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import echoedcore.utilities.FileUtilities;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  ClassCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class ClassCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getBotLogging().info("CLASS (called by " + mre.getAuthor().getAsTag() + ")");

        // If arg.length < 2 send classes list.
        // else find class in list.

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("class", "classlist");
    }

    @Override
    public String getModule() {
        return EchoedDungeonsConstants.DND;
    }

    @Override
    public String getDescription() {
        return "Lists all supported classes in EchoedDungeons' database.";
    }

    @Override
    public String getName() {
        return "Class Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + getAliases().get(0) + " [class name]`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    /**
     * Sends a message to the user's direct message
     * @param channel channel of the user's direct message
     * @param args args to pull from
     */
    private void sendPrivateMessage (PrivateChannel channel, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.

        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Classes Supported").setColor(Color.RED);

            MessageUtilities.addEmbedDefaults(embed);

            // For each command, add its values to embed.
            for (String classSupported : DatabaseManager.getSupportedClasses()) {
                String directory = "Database/Classes/" + classSupported.substring(0, 1).toUpperCase() + classSupported.substring(1).toLowerCase() + "/" + classSupported + ".json";

                DatabaseManager.addBasicInformation(embed, classSupported, directory);
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {

            String command = args[1].toLowerCase();
            // Check each command. If it is the command searched for, build embed.
            for (String classSupported : DatabaseManager.getSupportedClasses()) {
                String directory = "Database/Classes/" + classSupported.substring(0, 1).toUpperCase() + classSupported.substring(1).toLowerCase() + "/" + classSupported + ".json";

                String name = FileUtilities.getValueByKey(directory, "name", classSupported);
                if (name.toLowerCase().contains(command)) {
                    // Define values.
                    EmbedBuilder embed = DatabaseManager.getClassByName(command);

                    // Send embed.
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }
            // If it reaches this point, it does not exist.
            MessageUtilities.doesNotExist(channel, args[1], "class");
        }
    }
}
