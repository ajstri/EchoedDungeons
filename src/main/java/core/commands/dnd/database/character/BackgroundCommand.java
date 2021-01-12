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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import echoedcore.utilities.FileUtilities;
import utilities.EchoedDungeonsConstants;
import utilities.dnd.DatabaseManager;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  BackgroundCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class BackgroundCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getBotLogging().info("BACKGROUND (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("background", "bg");
    }

    @Override
    public String getModule() {
        return EchoedDungeonsConstants.DND;
    }

    @Override
    public String getDescription() {
        return "Returns information about a Background";
    }

    @Override
    public String getName() {
        return "Background Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + getAliases().get(0) + " [backgroundName]`");
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
    private void sendPrivateMessage(PrivateChannel channel, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Backgrounds Supported").setColor(Color.RED);

            MessageUtilities.addEmbedDefaults(embed);

            // For each command, add its values to embed.
            for (String backgroundSupported : DatabaseManager.getSupportedBackgrounds()) {
                String directory = "Database/Backgrounds/" + backgroundSupported.substring(0, 1).toUpperCase() + backgroundSupported.substring(1).toLowerCase() + "/" + backgroundSupported + ".json";

                DatabaseManager.addBasicInformation(embed, backgroundSupported, directory);
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {
            String command = args[1].toLowerCase();
            // Check each command. If it is the command searched for, build embed.
            for (String backgroundSupported : DatabaseManager.getSupportedBackgrounds()) {
                String directory = "Database/Backgrounds/" + backgroundSupported.substring(0, 1).toUpperCase() + backgroundSupported.substring(1).toLowerCase() + "/" + backgroundSupported + ".json";

                if (FileUtilities.checkIfFileExists(directory)) {
                    String name = FileUtilities.getValueByKey(directory, "name", backgroundSupported);
                    if (name.toLowerCase().contains(command)) {
                        // Define values.
                        EmbedBuilder embed = DatabaseManager.getBackgroundByName(command);

                        // Send embed.
                        channel.sendMessage(embed.build()).queue();
                        return;
                    }
                }
            }
            // If it reaches this point, it does not exist.
            MessageUtilities.doesNotExist(channel, args[1], "background");
        }
    }
}
