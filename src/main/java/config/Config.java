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
package config;

import core.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.*;
import utils.exceptions.NoConfigurationFileException;

import java.io.*;

/**
 *  Config class of the EchoedDungeons project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class Config {

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
            Logger.error("Configuration File is not usable.", new NoConfigurationFileException("Unusable configuration file."));
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
            Logger.error("Unable to create configuration file.", new NoConfigurationFileException("Unusable configuration file."));
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }
    }

    /**
     * Checks usability of Configuration file.
     * @return true if usable, false if not.
     */
    private boolean checkConfigurationUsability() {
        // TODO Config usability
        return true;
    }

    /**
     * Adds a JSON Array to the Configuration File.
     * @param object JSONObject to add to file.
     */
    @SuppressWarnings("unused")
    private int writeToFile(JSONArray object) {
        // Write to the file.
        return FileUtils.writeToFile(object, fileName);
    }

    /**
     * Adds a JSON Object to the Configuration File.
     * @param obj JSONObject to add to file.
     */
    @SuppressWarnings("Duplicates")
    private int writeToFile(JSONObject obj) {
        // Write to the file.
        return FileUtils.writeToFile(obj, fileName);
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    @SuppressWarnings("unused")
    private JSONArray getJSONFile() {
        return FileUtils.getJSONFile(fileName);
    }

    /**
     * Retrieves prefix for the Bot.
     * @return prefix from Configuration.
     */
    public String getPrefix() {
        String prefix = FileUtils.getValueByKey(fileName, Constants.PREFIX_KEY, arrayName);

        if (prefix.contains("" + Constants.STATUS_NO_CONFIG)) {
            Logger.info("No need for a shutdown. Failed to grab prefix. Using default.");
            return Constants.PREFIX_VALUE;
        }

        return prefix;
    }

    /**
     * Retrieves token for the Bot.
     * @return token from Configuration.
     */
    public String getToken() {
        String token = FileUtils.getValueByKey(fileName, Constants.TOKEN_KEY, arrayName);

        if (token.contains("" + Constants.STATUS_NO_CONFIG)) {
            Logger.info("No token found. Calling for shut down.");
            Logger.error("No Token in Configuration File.", new NoConfigurationFileException("Unusable configuration file. No Token provided."));
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
        String debug = FileUtils.getValueByKey(fileName, Constants.DEBUG_KEY, arrayName);

        if (debug.contains("" + Constants.STATUS_NO_CONFIG)) {
            Logger.info("Failed to grab debug. Using default.");
            return Constants.DEBUG_VALUE.toLowerCase().contains("true");
        }

        return debug.toLowerCase().contains("true");
    }

}
