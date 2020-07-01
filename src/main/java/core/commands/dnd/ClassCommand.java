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
package core.commands.dnd;

import core.Main;
import core.commands.Command;
import dnd.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.FileUtilities;
import utilities.MessageUtilities;

import javax.xml.crypto.Data;
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
        // If arg.length < 2 send classes list.
        // else find class in list.
        Main.getLog().info("CLASS (called by " + mre.getAuthor().getAsTag() + ")");

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
        return Constants.DND;
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
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "class [class name]`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    private void sendPrivateMessage (PrivateChannel channel, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Classes Supported").setColor(Color.RED);

            MessageUtilities.addEmbedDefaults(embed);

            String name;
            String wikiLink;

            // For each command, add its values to embed.
            for (String classSupported : DatabaseManager.getSupportedClasses()) {
                String directory = "Database/Classes/" + classSupported.substring(0, 1).toUpperCase() + classSupported.substring(1).toLowerCase() + "/" + classSupported + ".json";
                name = FileUtilities.getValueByKey(directory, "name", classSupported);
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                wikiLink = FileUtilities.getValueByKey(directory, "wiki link", classSupported);
                embed.addField(name, wikiLink, false);
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
            doesNotExist(channel, args);
        }
    }

    /**
     * Sends a message telling the user their search doesn't e@xexist
     * @param channel channel to send message
     * @param args arguments to build message
     */
    private static void doesNotExist(PrivateChannel channel, String[] args) {
        // If it reaches this point, the command searched for does not exist.
        channel.sendMessage(new MessageBuilder()
                .append("The provided class '**")
                .append(args[1])
                .append("**' does not exist. Use `")
                .append(Main.getConfig().getPrefix())
                .append("class` to list all classes.")
                .build()).queue();
    }
}
