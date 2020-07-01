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
import org.json.JSONObject;
import utilities.Constants;
import utilities.FileUtilities;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
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
    private static final String fileName = "Database/facts.json";
    private static final String arrayName = "facts";
    private static int factLength;

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("FACT (called by " + mre.getAuthor().getAsTag() + ")");

        // Get length of fact file
        JSONObject object = FileUtilities.getJSONFileObject(fileName);
        assert object != null;
        JSONObject innerObject = object.getJSONObject(arrayName);
        factLength = innerObject.length();

        File f = new File(fileName);
        if(!f.exists() || f.isDirectory()) {
            // File does not exist
            mre.getChannel().sendMessage("Fact list doesn't exist.").queue();
            return;
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
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "fact");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
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
}
