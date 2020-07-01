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
import utilities.FileUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;
import utilities.dnd.DatabaseManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  LanguageCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class LanguageCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("LANGUAGE (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("language", "lang", "languages");
    }

    @Override
    public String getModule() {
        return Constants.DND;
    }

    @Override
    public String getDescription() {
        return "Displays a list of supported languages";
    }

    @Override
    public String getName() {
        return "Language Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "language [language name]");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    /**
     * Sends a message to a private channel.
     * @param channel channel to send to
     * @param args arguments used to build the message.
     */
    private static void sendPrivateMessage(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            // ie. just language, no args
            // list languages
            EmbedBuilder embed = DatabaseManager.listAllLanguages();
            channel.sendMessage(embed.build()).queue();
        }
        else {
            // Asking for specific language?
            String lang = args[1].toLowerCase();

            for (String languageSupported : DatabaseManager.getSupportedLanguages()) {
                String directory = "Database/Languages/" + languageSupported.toLowerCase().replace(" ", "") + ".json";

                String name = FileUtilities.getValueByKey(directory, "name", languageSupported);
                if (name.toLowerCase().contains(lang)) {
                    // Define values.
                    EmbedBuilder embed = DatabaseManager.getLanguageByName(lang);
                    MessageUtilities.addEmbedDefaults(embed);

                    // Send embed.
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }
            // If it reaches this point, it doesn't exist
            MessageUtilities.doesNotExist(channel, args[1], "language");
        }
    }
}
