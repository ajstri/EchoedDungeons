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
import utilities.dnd.DatabaseManager;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;

import java.util.*;

/**
 *  FeatureCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class FeatureCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        // If arg.length < 2 send command info.
        // if arg.length < 3 send features list for a class.
        // if arg.length < 4 send feature info for the given class and feature.x
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
        if (args.length < 2) {
            // Command Info
            channel.sendMessage("In order to use this command, please provide the class name and feature name:\n`" +
                    Main.getConfig().getPrefix() + "feature [className] [featureName]`").queue();
        }
        else if (args.length < 3) {
            // Features List By Class
            String className = args[1].toLowerCase();
            channel.sendMessage(DatabaseManager.listClassFeatures(className).build()).queue();
        }
        else {
            // Feature Info By Class & Feature Name
            String className = args[1].toLowerCase();
            StringBuilder featureName = new StringBuilder();

            for (int i = 2; i < args.length; i++) {
                featureName.append(args[i]).append(" ");
            }

            channel.sendMessage(DatabaseManager.getFeatureByName(featureName.toString().substring(0, featureName.length() - 1), className).build()).queue();
        }
    }

}
