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
package config;

import core.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.*;
import utilities.exceptions.NoConfigurationFileException;

import java.io.*;

/**
 *  Config class of the EchoedDungeons project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class Config {
    // TODO add Shards

    /**
     * File name constants
     */
    private final static String fileName = "config.json";
    private final static String arrayName = "bot";

    /**
     * Constructor for the Config Class
     */
    public Config() {
        // Check if Configuration File exists.
        File f = new File(fileName);
        if(!f.exists() || f.isDirectory()) {
            // Create file.
            createConfigurationFile();
        }

        // Check if Configuration File is usable.
        if(!checkConfigurationUsability()) {
            Main.getLog().error("Configuration File is not usable.", new NoConfigurationFileException("Unusable configuration file."));
            Main.shutdown(Constants.STATUS_CONFIG_UNUSABLE);
        }
    }

    /**
     * Create the Configuration File from default keys and values.
     */
    private void createConfigurationFile() {
        // Create Bot config options.
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        // Put default keys and values into the object.
        object.put(Constants.TOKEN_KEY, Constants.TOKEN_VALUE);
        object.put(Constants.PREFIX_KEY, Constants.PREFIX_VALUE);
        object.put(Constants.DEBUG_KEY, Constants.DEBUG_VALUE);
        object.put(Constants.GAME_STATUS_KEY, Constants.GAME_STATUS_VALUE);
        object.put(Constants.SHARDS_KEY, Constants.SHARDS_VALUE);

        array.put(object);

        JSONObject obj2 = new JSONObject();
        obj2.put(arrayName, object);

        // Write to the file.
        if(writeToFile(obj2) < 0) {
            Main.getLog().error("Unable to create configuration file.", new NoConfigurationFileException("Unusable configuration file."));
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }
    }

    /**
     * Checks usability of Configuration file.
     * @return true if usable, false if not.
     */
    private boolean checkConfigurationUsability() {
        // The only thing, so far, that would break
        // the bot's functionality is the Token.
        // Everything else can be set to default.
        return !getToken().contains(Constants.TOKEN_VALUE);
    }

    /**
     * Adds a JSON Array to the Configuration File.
     * @param object JSONObject to add to file.
     */
    @SuppressWarnings("unused")
    private int writeToFile(JSONArray object) {
        // Write to the file.
        return FileUtilities.writeToFile(object, fileName);
    }

    /**
     * Adds a JSON Object to the Configuration File.
     * @param obj JSONObject to add to file.
     */
    private int writeToFile(JSONObject obj) {
        // Write to the file.
        return FileUtilities.writeToFile(obj, fileName);
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    @SuppressWarnings("unused")
    private JSONArray getJSONFile() {
        return FileUtilities.getJSONFileArray(fileName);
    }

    /**
     * Retrieves prefix for the Bot.
     * @return prefix from Configuration.
     */
    public String getPrefix() {
        String prefix = FileUtilities.getValueByKey(fileName, Constants.PREFIX_KEY, arrayName);

        if (prefix.contains("" + Constants.STATUS_NO_CONFIG)) {
            Main.getLog().info("No need for a shutdown. Failed to grab prefix. Using default.");
            return Constants.PREFIX_VALUE;
        }

        return prefix;
    }

    /**
     * Sets a new prefix for the Bot to use.
     * @param newPrefix new prefix for the Bot to use
     */
    public void setPrefix(String newPrefix) {
        JSONArray jsonArray = FileUtilities.getJSONFileArray(fileName);
        JSONObject object =  jsonArray.getJSONObject(0).getJSONObject(arrayName);
        object.put(Constants.PREFIX_KEY, newPrefix);

        JSONArray array = new JSONArray().put(object);

        JSONObject bot = new JSONObject().put(arrayName, array);
        writeToFile(bot);
    }

    /**
     * Retrieves token for the Bot.
     * @return token from Configuration.
     */
    public String getToken() {
        String token = FileUtilities.getValueByKey(fileName, Constants.TOKEN_KEY, arrayName);

        if (token.contains("" + Constants.STATUS_NO_CONFIG)) {
            Main.getLog().info("No token found. Calling for shut down.");
            Main.getLog().error("No Token in Configuration File.", new NoConfigurationFileException("Unusable configuration file. No Token provided."));
            Main.shutdown(Constants.STATUS_NO_CONFIG);
            //return Constants.TOKEN_VALUE; // no actual use
        }

        return token;
    }

    /**
     * Retrieves debug status for the Bot.
     * @return debug status from Configuration.
     */
    public boolean getDebug() {
        String debug = FileUtilities.getValueByKey(fileName, Constants.DEBUG_KEY, arrayName);

        if (debug.contains("" + Constants.STATUS_NO_CONFIG)) {
            Main.getLog().info("Failed to grab debug. Using default.");
            return Constants.DEBUG_VALUE.toLowerCase().contains("true");
        }

        return debug.toLowerCase().contains("true");
    }

    /**
     * Sets a new debug value for the Bot.
     * @param debug true if debug is on, false if not
     */
    public void setDebug(boolean debug) {
        JSONArray jsonArray = FileUtilities.getJSONFileArray(fileName);
        JSONObject object =  jsonArray.getJSONObject(0).getJSONObject(arrayName);
        object.put(Constants.DEBUG_KEY, debug);

        JSONArray array = new JSONArray().put(object);

        JSONObject bot = new JSONObject().put(arrayName, array);
        writeToFile(bot);
    }

    /**
     * Returns if the math mode is degrees or radians.
     * @return true if degrees, false if radians
     */
    public boolean getDegrees() {
        String debug = FileUtilities.getValueByKey(fileName, Constants.MATHMODE_KEY, arrayName);

        if (debug.contains("" + Constants.STATUS_NO_CONFIG)) {
            Main.getLog().info("Failed to grab degrees. Using default.");
            return Constants.MATHMODE_VALUE.toLowerCase().contains("degrees");
        }

        return debug.toLowerCase().contains("degrees");
    }

    /**
     * Sets a new math mode value for the Bot.
     * @param degrees true if degrees, false if not
     */
    public void setDegrees(boolean degrees) {
        JSONArray jsonArray = FileUtilities.getJSONFileArray(fileName);
        JSONObject object =  jsonArray.getJSONObject(0).getJSONObject(arrayName);

        String degreesString;
        if (degrees) degreesString = "degrees";
        else degreesString = "radians";

        object.put(Constants.MATHMODE_KEY, degreesString);

        JSONArray array = new JSONArray().put(object);

        JSONObject bot = new JSONObject().put(arrayName, array);
        writeToFile(bot);
    }

}