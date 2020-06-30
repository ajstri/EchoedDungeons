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
import dnd.other.features.Feature;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *  FeatureCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
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
        Main.getLog().info("FEATURE (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("feature", "featureinfo", "finfo");
    }

    @Override
    public String getModule() {
        return Constants.DND;
    }

    @Override
    public String getDescription() {
        return "Displays a list of supported features.";
    }

    @Override
    public String getName() {
        return "Feature Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "feature [feature name]");
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
    private void sendPrivateMessage (PrivateChannel channel, String[] args) {
        int page = 1;

        // If arg.length < 2 send classes list.
        // else find class in list.
        if (args.length < 2) { // No page number, show first 10
            EmbedBuilder embed = new EmbedBuilder().setTitle("Features Supported: Page 1").setColor(Color.RED);
            MessageUtilities.addEmbedDefaults(embed);
            featuresListByPage(embed, page, channel);
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();
            MessageUtilities.addEmbedDefaults(embed);

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
                .append("The provided feature '**")
                .append(args[1])
                .append("**' does not exist. Use `")
                .append(Main.getConfig().getPrefix())
                .append("feature` to list all features.")
                .build()).queue();
    }

    /**
     * Adds feature values to the embed.
     * @param embed embed to add values to
     * @param f feature to add
     */
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

    /**
     * Lists features by page.
     * @param embed embed to build
     * @param page page to list
     * @param channel channel to send to
     */
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
