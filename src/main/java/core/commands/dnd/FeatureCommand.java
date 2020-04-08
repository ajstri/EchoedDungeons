package core.commands.dnd;

import core.commands.Command;
import dndinfo.other.Features.Feature;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.EmbedUtils;
import utils.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;

public class FeatureCommand extends Command {

    private static final String DEFAULT_NAME = "No name has been provided. Sorry!";
    private static final String DEFAULT_INFO = "No information has been provided. Sorry!";

    public final TreeMap<String, Feature> features;

    public FeatureCommand() {
        features = new TreeMap<>();
    }

    public void registerFeature(Feature feature) {
        features.put(feature.getName(), feature);
    }

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.
        Logger.info("FEATURE");

        // Bypass sending message if it is already in a private message.
        if(!mre.isFromType(ChannelType.PRIVATE)) {
            // Send help message
            mre.getTextChannel().sendMessage(new MessageBuilder()
                    .append("Hey, ")
                    .append(mre.getAuthor())
                    .append(": Help information was sent as a private message.")
                    .build()).queue();
        }
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("feature", "featureinfo", "finfo");
    }

    @Override
    public boolean isDND() {
        return true;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "Feature Command";
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }

    private void sendPrivateMessage (PrivateChannel channel, String[] args) {
        int page = 1;

        // If arg.length < 2 send classes list.
        // else find class in list.
        if (args.length < 2) { // No page number, show first 10
            EmbedBuilder embed = new EmbedBuilder().setTitle("Features Supported: Page 1").setColor(Color.RED);
            EmbedUtils.addDefaults(embed);
            featuresListByPage(embed, page, channel);
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();
            EmbedUtils.addDefaults(embed);

            try {
                page = Integer.parseInt(args[1]);
                embed.setTitle("Features Supported: Page " + page).setColor(Color.RED);
                featuresListByPage(embed, page, channel);
            }
            catch (NumberFormatException nfe) {
                String command = args[1].toLowerCase();
                // Check each command. If it is the command searched for, build embed.
                for (Feature f : features.values()) {
                    if (f.getName().toLowerCase().split(" ")[0].contains(command)) {
                        // Define values.
                        addFeatureValues(embed, f);

                        // Send embed.
                        channel.sendMessage(embed.build()).queue();
                        return;

                    }
                }
            }
            // TODO doesNotExist
        }
    }

    private void addFeatureValues(EmbedBuilder embed, Feature f) {
        String info;
        String name;

        embed.addField("Required Class Level", f.getLevel() + "", false);

        if (f.getName() == null) {
            name = DEFAULT_NAME;
        }
        else {
            name = f.getName();
        }
        if (f.getInfo() == null) {
            info = DEFAULT_INFO;
            embed.addField(info, "", false);
        }
        else {
            for (String infoPiece : f.getInfo()) {
                embed.addField("", infoPiece, false);
            }
        }

        embed.setTitle("Feature: " + name);
    }

    private void featuresListByPage (EmbedBuilder embed, int page, PrivateChannel channel) {
        boolean sendEmbed = true;
        String name;

        Set<String> keys = features.keySet();
        String[] keysAsString = new String[keys.size()];

        int k = 0;
        for (String i: keys) {
            keysAsString[k++] = i;
        }

        for (int i = (page - 1) * 10; i < ((page - 1 ) * 10) + 10; i++) {
            // For each command, add its values to embed.
            // Add 9 depending on the page
            try {
                if (features.get(keysAsString[i]).getName() == null) {
                    name = DEFAULT_NAME;
                }
                else {
                    name = features.get(keysAsString[i]).getName();
                }

                embed.addField(name, "", false);
            }
            catch (IndexOutOfBoundsException iobe) {
                if (i == (page - 1) * 10) {
                    channel.sendMessage("No features on this page!").queue();
                    sendEmbed = false;
                }
                break;
            }
        }
        // Send embed.
        if (sendEmbed) channel.sendMessage(embed.build()).queue();
    }
}
