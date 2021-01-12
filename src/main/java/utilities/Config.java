package utilities;

import core.Main;
import echoedcore.core.EchoedCore;
import echoedcore.configuration.Configuration;
import echoedcore.utilities.Constants;
import echoedcore.utilities.FileUtilities;
import echoedcore.utilities.exceptions.NoConfigurationFileException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Config extends Configuration {

    public Config() {
        File f = new File(getFileName());
        if (!f.exists() || f.isDirectory()) {
            // Create file.
            createConfigurationFile();
        }
    }

    /**
     * Create the Configuration File from default keys and values.
     */
    private void createConfigurationFile() {
        // Create Bot config options.
        JSONArray array = FileUtilities.getJSONFileArray(getFileName());
        JSONObject object =  array.getJSONObject(0).getJSONObject(getArrayName());

        // Put default keys and values into the object.
        object.put(EchoedDungeonsConstants.MATHMODE_KEY, EchoedDungeonsConstants.MATHMODE_VALUE);


        // Write to the file.
        JSONObject bot = new JSONObject().put(getArrayName(), array);
        if (writeToFile(bot) < 0) {
            Main.getBotLogging().error("Unable to create configuration file.", new NoConfigurationFileException("Unusable configuration file."));
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }
    }

    /**
     * Returns if the math mode is degrees or radians.
     * @return true if degrees, false if radians
     */
    public boolean getDegrees() {
        String debug = FileUtilities.getValueByKey(getFileName(), EchoedDungeonsConstants.MATHMODE_KEY, getArrayName());

        if (debug.contains("" + Constants.STATUS_NO_CONFIG)) {
            Main.getBotLogging().info("Failed to grab degrees. Using default.");
            return EchoedDungeonsConstants.MATHMODE_VALUE.toLowerCase().contains("degrees");
        }

        return debug.toLowerCase().contains("degrees");
    }

    /**
     * Sets a new math mode value for the Bot.
     * @param degrees true if degrees, false if not
     */
    public void setDegrees(boolean degrees) {
        JSONArray jsonArray = FileUtilities.getJSONFileArray(getFileName());
        JSONObject object =  jsonArray.getJSONObject(0).getJSONObject(getArrayName());

        String degreesString;
        if (degrees) degreesString = "degrees";
        else degreesString = "radians";

        object.put(EchoedDungeonsConstants.MATHMODE_KEY, degreesString);

        JSONArray array = new JSONArray().put(object);

        JSONObject bot = new JSONObject().put(getArrayName(), array);
        writeToFile(bot);
    }
}
