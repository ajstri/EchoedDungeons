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
import utilities.dnd.DNDConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
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

    private static final String[] common = new String[] { DNDConstants.COMMON, "Humans", DNDConstants.COMMON_SCRIPT };
    private static final String[] dwarvish = new String[] { DNDConstants.DWARVISH, "Dwarves", DNDConstants.ELVISH_SCRIPT };
    private static final String[] elvish = new String[] { DNDConstants.ELVISH, "Elves", DNDConstants.COMMON_SCRIPT };
    private static final String[] giant = new String[] { DNDConstants.GIANT, "Ogres, Giants", DNDConstants.DWARVISH_SCRIPT };
    private static final String[] gnomish = new String[] { DNDConstants.GNOMISH, "Gnomes", DNDConstants.DWARVISH_SCRIPT };
    private static final String[] goblin = new String[] { DNDConstants.GOBLIN, "Goblinoids", DNDConstants.DWARVISH_SCRIPT };
    private static final String[] halfling = new String[] { DNDConstants.HALFLING, "Halflings", DNDConstants.COMMON_SCRIPT };
    private static final String[] orc = new String[] { DNDConstants.ORC, "Orcs", DNDConstants.DWARVISH_SCRIPT };

    private static final String[] abyssal = new String[] { DNDConstants.ABYSSAL, "Demons", DNDConstants.INFERNAL_SCRIPT };
    private static final String[] celestial = new String[] { DNDConstants.CELESTIAL, "Celestials", DNDConstants.CELESTIAL_SCRIPT };
    private static final String[] draconic = new String[] { DNDConstants.DRACONIC, "Dragons, Dragonborn", DNDConstants.DRACONIC_SCRIPT };
    private static final String[] deep_speech = new String[] { DNDConstants.DEEP_SPEECH, "Aboleths, Cloakers", "N/A" };
    private static final String[] infernal = new String[] { DNDConstants.INFERNAL, "Devils", DNDConstants.INFERNAL_SCRIPT };
    private static final String[] primordial = new String[] { DNDConstants.PRIMORDIAL, "Elementals", DNDConstants.DWARVISH_SCRIPT };
    private static final String[] sylvan = new String[] { DNDConstants.SYLVAN, "Fey", DNDConstants.ELVISH_SCRIPT };
    private static final String[] undercommon = new String[] { DNDConstants.UNDERCOMMON, "Underworld Traders", DNDConstants.ELVISH_SCRIPT };

    private static final String[][] languages = new String[][] {
            common, dwarvish, elvish,
            giant, gnomish, goblin,
            halfling, orc, abyssal,
            celestial, draconic, deep_speech,
            infernal, primordial, sylvan,
            undercommon
    };

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("LANGUAGE (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
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
            doesNotExist(channel, args);
        }
    }

    /**
     * Sends a message telling the user their search doesn't exist
     * @param channel channel to send message
     * @param args arguments to build message
     */
    private static void doesNotExist(PrivateChannel channel, String[] args) {
        // If it reaches this point, the command searched for does not exist.
        channel.sendMessage(new MessageBuilder()
                .append("The provided language '**")
                .append(args[1])
                .append("**' does not exist. Use `")
                .append(Main.getConfig().getPrefix())
                .append("language` to list all languages.")
                .build()).queue();
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
}
