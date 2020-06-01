package core.commands.dnd;

import core.Main;
import core.commands.Command;
import dndinfo.other.DNDConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Constants;
import utils.Logger;
import utils.MessageUtils;

import java.util.Arrays;
import java.util.List;

public class LanguageCommand extends Command {

    private static String[][] languages;

    public LanguageCommand () {
        registerLanguages();
    }

    private static void registerLanguages() {
        String[] common = new String[] { DNDConstants.COMMON, "Humans", DNDConstants.COMMON_SCRIPT };
        String[] dwarvish = new String[] { DNDConstants.DWARVISH, "Dwarves", DNDConstants.ELVISH_SCRIPT };
        String[] elvish = new String[] { DNDConstants.ELVISH, "Elves", DNDConstants.COMMON_SCRIPT };
        String[] giant = new String[] { DNDConstants.GIANT, "Ogres, Giants", DNDConstants.DWARVISH_SCRIPT };
        String[] gnomish = new String[] { DNDConstants.GNOMISH, "Gnomes", DNDConstants.DWARVISH_SCRIPT };
        String[] goblin = new String[] { DNDConstants.GOBLIN, "Goblinoids", DNDConstants.DWARVISH_SCRIPT };
        String[] halfling = new String[] { DNDConstants.HALFLING, "Halflings", DNDConstants.COMMON_SCRIPT };
        String[] orc = new String[] { DNDConstants.ORC, "Orcs", DNDConstants.DWARVISH_SCRIPT };

        String[] abyssal = new String[] { DNDConstants.ABYSSAL, "Demons", DNDConstants.INFERNAL_SCRIPT };
        String[] celestial = new String[] { DNDConstants.CELESTIAL, "Celestials", DNDConstants.CELESTIAL_SCRIPT };
        String[] draconic = new String[] { DNDConstants.DRACONIC, "Dragons, Dragonborn", DNDConstants.DRACONIC_SCRIPT };
        String[] deep_speech = new String[] { DNDConstants.DEEP_SPEECH, "Aboleths, Cloakers", "N/A" };
        String[] infernal = new String[] { DNDConstants.INFERNAL, "Devils", DNDConstants.INFERNAL_SCRIPT };
        String[] primordial = new String[] { DNDConstants.PRIMORDIAL, "Elementals", DNDConstants.DWARVISH_SCRIPT };
        String[] sylvan = new String[] { DNDConstants.SYLVAN, "Fey", DNDConstants.ELVISH_SCRIPT };
        String[] undercommon = new String[] { DNDConstants.UNDERCOMMON, "Underworld Traders", DNDConstants.ELVISH_SCRIPT };

        languages = new String[][] {
                common, dwarvish, elvish,
                giant, gnomish, goblin,
                halfling, orc, abyssal,
                celestial, draconic, deep_speech,
                infernal, primordial, sylvan,
                undercommon
        };
    }

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("LANGUAGE (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtils.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    private static void sendPrivateMessage(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            // ie. just language, no args
            // list languages
            EmbedBuilder embed = new EmbedBuilder();

            MessageUtils.addDefaults(embed);

            for (String[] language : languages) {
                embed.addField(language[0], language[2], true);
            }
            channel.sendMessage(embed.build()).queue();
        }
        else {
            // Asking for specific language?
            EmbedBuilder embed = new EmbedBuilder();

            MessageUtils.addDefaults(embed);
            MessageUtils.setTimestamp(embed);

            String lang = args[1];
            for (String[] language : languages) {
                if (language[0].toLowerCase().contains(lang)) {
                    embed.addField("Language", language[0], true);
                    embed.addField("Commonly Spoken By", language[1], true);
                    embed.addField("Script", language[2], true);
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }
            // if it reaches this point, it does not exist
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
                .append(Main.config.getPrefix())
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
        return "";
    }

    @Override
    public String getName() {
        return "Language Command";
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
