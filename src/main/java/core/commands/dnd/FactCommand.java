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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.Constants;
import utilities.FileUtilities;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *  FactCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class FactCommand extends Command {

    /**
     * File constants
     */
    private static final String fileName = "facts.json";
    private static final String arrayName = "facts";
    private static int factLength;

    public static final String[] facts = new String[]{
            "Add your Dexterity modifier to your Initiative",
            "Finesse weapons can use Dexterity instead of Strength",
            "AC (while unarmored) is 10 + Dex UNLESS your class states otherwise",
            "All ranged weapons use Dexterity",
            "Shields add +1 to your AC",
            "It's generally a good idea to have Constitution as your second highest stat",
            "Cantrips do not use spell slots",
            "Not every situation has to be resolved through combat",
            "You can spend any amount of hit dice to regain HP during a short rest. They recharge every long rest",
            "Your proficiency bonus doesn't stack with each new level.",
            "Don't meta too much! Keep most of the puzzle solving and interactions in-character. Don't do stuff in-game that your characters wouldn't realistically know"
    };

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("FACT (called by " + mre.getAuthor().getAsTag() + ")");

        factLength = facts.length;

        File f = new File(fileName);
        if(!f.exists() || f.isDirectory()) {
            // Create file.
            mre.getChannel().sendMessage("Please hold on! I need to recreate my fact file.").queue();
            createRandomFactFile();
        }

        mre.getChannel().sendMessage(getRandomFact()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fact", "f");
    }

    @Override
    public String getModule() {
        return Constants.DND;
    }

    @Override
    public String getDescription() {
        return "Returns a useful & random D&D fact!";
    }

    @Override
    public String getName() {
        return "Random Fact Command";
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }

    /**
     * Creates .json file for the Random Facts
     */
    private void createRandomFactFile() {
        // Create Bot config options.
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        // Put default keys and values into the object.
        for (int i = 1; i < facts.length; i++) {
            object.put("" + i, facts[i]);
        }

        array.put(object);

        JSONObject obj2 = new JSONObject();
        obj2.put(arrayName, object);

        // Write to the file.
        if(writeToFile(obj2) == Constants.WRITE_TO_FILE_FAIL) {
            Main.getLog().error("Unable to create Random Fact File.", new Exception());
        }
    }

    /**
     * Gets a random fact from the Fact File
     * @return a random fact
     */
    private String getRandomFact() {
        Random r = new Random();
        int result = r.nextInt(factLength - 1) + 1;

        return FileUtilities.getValueByKey(fileName, "" + result, arrayName);
    }

    /**
     * Writes to .json file
     * Accesses {@link FileUtilities#writeToFile(JSONObject obj, String fileName)}
     * @param obj Object to write to file
     * @return Success or fail calls
     */
    private int writeToFile(JSONObject obj) {
        return FileUtilities.writeToFile(obj, fileName);
    }
}
