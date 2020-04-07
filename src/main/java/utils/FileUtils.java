package utils;

import core.Main;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    /**
     * Adds a JSON Array to the Configuration File.
     * @param obj JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    public static int writeToFile(JSONObject obj, String fileName) {
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
            return Constants.WRITE_TO_FILE_SUCCESS;
        }
        catch (IOException ioe) {
            Logger.error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    /**
     * Adds a JSON Array to the Configuration File.
     * @param object JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    public static int writeToFile(JSONArray object, String fileName) {
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
            return Constants.WRITE_TO_FILE_SUCCESS;
        }
        catch (IOException ioe) {
            Logger.error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    public static String getValueByKey(String fileName, String key, String arrayLocation) {
        JSONArray object = getJSONFile(fileName);

        if (object.equals(null)) {
            Logger.error(key + " is null.", new Exception("Failed to grab " + key));
            return  "" + Constants.STATUS_NO_CONFIG;
        }

        JSONObject jsonObject = object.getJSONObject(0);
        JSONObject array = (JSONObject)jsonObject.opt(arrayLocation);

        return array.optString(key);
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    public static JSONArray getJSONFile(String fileName) {
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
            Logger.error("File not found.", fnfe);
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }
        catch (Exception e) {
            Logger.error("File could not be read.", e);
            Main.shutdown(Constants.STATUS_NO_CONFIG);
        }

        return array;

    }

}
