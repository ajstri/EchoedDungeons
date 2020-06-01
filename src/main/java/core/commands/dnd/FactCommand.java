/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package core.commands.dnd;

import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Constants;
import utils.FactList;
import utils.FileUtils;
import utils.Logger;

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

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("FACT (called by " + mre.getAuthor().getAsTag() + ")");

        factLength = FactList.facts.length;

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
     * Creates .json file for the Random Facts, held in
     * {@link FactList}
     */
    private void createRandomFactFile() {
        // Create Bot config options.
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        // Put default keys and values into the object.
        for (int i = 1; i < FactList.facts.length; i++) {
            object.put("" + i, FactList.facts[i]);
        }

        array.put(object);

        JSONObject obj2 = new JSONObject();
        obj2.put(arrayName, object);

        // Write to the file.
        if(writeToFile(obj2) == Constants.WRITE_TO_FILE_FAIL) {
            Logger.error("Unable to create Random Fact File.", new Exception());
        }
    }

    /**
     * Gets a random fact from the Fact File
     * @return a random fact
     */
    private String getRandomFact() {
        Random r = new Random();
        int result = r.nextInt(factLength - 1) + 1;

        return FileUtils.getValueByKey(fileName, "" + result, arrayName);
    }

    /**
     * Writes to .json file
     * Accesses {@link FileUtils#writeToFile(JSONObject obj, String fileName)}
     * @param obj Object to write to file
     * @return Success or fail calls
     */
    private int writeToFile(JSONObject obj) {
        return FileUtils.writeToFile(obj, fileName);
    }
}
