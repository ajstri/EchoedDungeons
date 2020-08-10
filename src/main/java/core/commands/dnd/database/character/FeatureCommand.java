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
import core.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import utilities.FileUtilities;
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
        Main.getLog().info("FEATURE (called by " + mre.getAuthor().getAsTag() + ")");

        /*
         * If arg.length < 2 send command info.
         * if arg.length < 3 send features list for a class.
         * if arg.length < 4 send feature info for the given class and feature.
         */

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
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + getAliases().get(0) + " [feature name]");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    /**
     * Sends a message to a private channel.
     *
     * @param channel channel to send to
     * @param args    arguments used to build the message.
     */
    private void sendPrivateMessage(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            // Command Info
            Main.getLog().info("Command Information sent.");
            channel.sendMessage("In order to use this command, please provide the class name and feature name:\n`" +
                    Main.getConfig().getPrefix() + "feature [className] [featureName]`").queue();
        }
        else if (args.length < 3) {
            // Features List By Class
            // TODO add subclass support
            Main.getLog().info("Sending list of features for: " + args[1]);
            channel.sendMessage(DatabaseManager.listClassFeatures(args[1].toLowerCase()).build()).queue();
        }
        else {
            // Feature Info By Class & Feature Name
            String className = args[1].toLowerCase();
            StringBuilder featureName = new StringBuilder();

            // Put together the feature name into one string object
            for (int i = 2; i < args.length; i++) {
                if (i == args.length - 1) {
                    featureName.append(args[i]);
                }
                else featureName.append(args[i]).append(" ");
            }

            Main.getLog().info("Sending information for a specific feature. Class name given is " + className + " and the feature given is " + featureName.toString());

            // TODO add background feature support
            // Check each class for given class name. If match command, find feature
            // If fail: Check each class's subclass for given class name. If match command, find feature
            // If fail: does not exist

            // Check if file exists as a class.
            String directoryAsClass = "Database/Classes/" + className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase() + "/" + className.toLowerCase().replace(" ", "") + ".json";
            if (FileUtilities.checkIfFileExists(directoryAsClass)) {
                Main.getLog().debug("Found the class file.", Constants.stageCommand);
                // Class file exists, get supported features
                String featureDirectory = "Database/Classes/" + className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase() + "/" + className.toLowerCase().replace(" ", "") + "features.json";

                // Loop through supported features
                for (String featureSupported : DatabaseManager.getSupportedFeaturesByClass(className)) {
                    if (FileUtilities.checkIfFileExists(featureDirectory)) {
                        String name = FileUtilities.getValueByKey(featureDirectory, "name", featureSupported);

                        // It exists as a feature of Class:
                        if (name.toLowerCase().contains(featureName)) { // It exists
                            // Define values.
                            Main.getLog().debug("Found the feature.", Constants.stageCommand);
                            EmbedBuilder embed = DatabaseManager.getClassFeatureByName(featureName.toString(), className);
                            MessageUtilities.addEmbedDefaults(embed);

                            Main.getLog().info("Sending embed.");
                            // Send embed.
                            channel.sendMessage(embed.build()).queue();
                            return;
                        }
                    }
                }
                Main.getLog().debug("Scanning all subclasses of the given class.", Constants.stageCommand);
                // Loop through subclasses of className
                // It might exists as a feature of a subclass of Class:
                if (checkSubclass(channel, featureName, className)) return;
            }

            // Check all class' subclasses
            Main.getLog().debug("Scanning all subclasses of every class.", Constants.stageCommand);
            for (String classSupported : DatabaseManager.getSupportedClasses()) {
                // loop through subclasses
                if (checkSubclass(channel, featureName, classSupported)) return;
            }

            // if it reaches this point, it doesn't exist
            Main.getLog().debug("The given feature/class combination did not exist.", Constants.stageCommand);
            MessageUtilities.doesNotExist(channel, featureName.toString(), "feature");
        }
    }

    /**
     * Checks all subclasses for a given class
     * @param channel channel to send embed to
     * @param featureName name of feature to search for
     * @param classSupported root class to search subclasses of
     * @return true if sent, false if not
     */
    private boolean checkSubclass(PrivateChannel channel, StringBuilder featureName, String classSupported) {
        for (String subclassSupported : DatabaseManager.getSupportedSubclassesByClass(classSupported)) {
            String subclassFeatureDirectory = "Database/Classes/"
                    + classSupported.substring(0, 1).toUpperCase() + classSupported.substring(1).toLowerCase()
                    + "/Subclasses/" + subclassSupported.substring(0, 1).toUpperCase() + subclassSupported.substring(1).toLowerCase()
                    + "/" + subclassSupported.toLowerCase() + "features.json";
            Main.getLog().debug("Scanning subclass: " + subclassSupported, Constants.stageCommand);
            Main.getLog().debug("The directory is:\n" + subclassFeatureDirectory, Constants.stageCommand);
            // check features
            for (String subclassFeature : DatabaseManager.getSupportedFeaturesBySubclass(classSupported, subclassSupported)) {
                if (FileUtilities.checkIfFileExists(subclassFeatureDirectory)) {
                    String name = FileUtilities.getValueByKey(subclassFeatureDirectory, "name", subclassFeature);

                    // It exists as a feature of Class:
                    if (name.toLowerCase().contains(featureName)) { // It exists
                        Main.getLog().debug("The feature exists.", Constants.stageCommand);
                        // Define values.
                        EmbedBuilder embed = DatabaseManager.getSubclassFeatureByName(featureName.toString(), subclassSupported, classSupported);
                        MessageUtilities.addEmbedDefaults(embed);

                        Main.getLog().info("Sending embed.");
                        // Send embed.
                        channel.sendMessage(embed.build()).queue();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}