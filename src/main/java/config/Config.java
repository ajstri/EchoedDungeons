/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

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

import java.io.*;

public class Config {

    private final static String fileName = "config.json";

    public Config() {
        // Check if Configuration File exists.
        File f = new File(fileName);
        if(!f.exists() || f.isDirectory()) {
            // Create file.
            createConfigurationFile();
        }

        // Check if Configuration File is usable.
        if(!checkConfigurationUsability()) {
            Logger.error("Configuration File is not usable.", new Exception());
            Main.shutdown(Constants.STATUS_CONFIG_UNUSABLE);
        }
    }

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
        obj2.put("bot", object);

        // Write to the file.
        if(writeToFile(obj2) < 0) {
            Logger.error("Unable to create Configuration File.", new Exception());
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }
    }

    private boolean checkConfigurationUsability() {
        // TODO config usability
        return true;
    }

    /**
     * Adds a JSON Array to the Configuration File.
     * @param object JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    private int writeToFile(JSONArray object) {
        // Write to the file.
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(
                    object.toString()
                            .replace("[", "[\n")
                            .replace("{", "{\n")
                            .replace(",", ",\n")
                            .replace("}", "\n}")
                            .replace("]", "\n]")
            );
            fileWriter.flush();
            fileWriter.close();
            return 1;
        }
        catch (IOException ioe) {
            Logger.error("Unable to write to file.", ioe);
            return -1;
        }
    }

    /**
     * Adds a JSON Object to the Configuration File.
     * @param obj JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    private int writeToFile(JSONObject obj) {
        // Write to the file.
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(
                    obj.toString()
                            .replace("[", "[\n")
                            .replace("{", "{\n")
                            .replace(",", ",\n")
                            .replace("}", "\n}")
                            .replace("]", "\n]")
            );
            fileWriter.flush();
            fileWriter.close();
            return 1;
        }
        catch (IOException ioe) {
            Logger.error("Unable to write to file.", ioe);
            return -1;
        }
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    private JSONArray getJSONFile() {
        JSONObject obj;
        JSONArray array = new JSONArray();
        StringBuilder sb = new StringBuilder();

        try {
            FileReader reader = new FileReader(fileName);

            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }

            obj = new JSONObject(sb.toString().replace("[", "").replace("]", ""));
            array.put(obj);

        }
        catch (FileNotFoundException fnfe) {
            Logger.error("Configuration File not found.", fnfe);
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }
        catch (Exception e) {
            Logger.error("Configuration File could not be read.", e);
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }

        return array;

    }

    /**
     * Retrieves prefix for the Bot.
     * @return prefix from Configuration
     */
    @SuppressWarnings("ConstantConditions")
    public String getPrefix() {
        JSONArray object = getJSONFile();

        if (object.equals(null)) {
            Logger.error("Prefix is null.", new Exception());
            return Constants.PREFIX_VALUE;
        }

        JSONObject jsonObject = object.getJSONObject(0);
        JSONObject array = (JSONObject)jsonObject.opt("bot");

        return array.optString(Constants.PREFIX_KEY);
    }

    /**
     * Retrieves token for the Bot.
     * @return token from Configuration
     */
    @SuppressWarnings("ConstantConditions")
    public String getToken() {
        JSONArray object = getJSONFile();

        if (object.equals(null)) {
            Logger.error("Configuration file is null.", new Exception());
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }

        JSONObject jsonObject = object.getJSONObject(0);
        JSONObject array = (JSONObject)jsonObject.opt("bot");

        return array.optString(Constants.TOKEN_KEY);
    }

}
