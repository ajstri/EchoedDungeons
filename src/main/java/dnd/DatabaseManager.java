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
package dnd;

import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import utilities.FileUtilities;
import utilities.MessageUtilities;

import java.io.File;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {

    /**
     * Defaults
     */
    private static final String DEFAULT_NAME = "No name provided. Sorry!";
    private static final String DEFAULT_WIKI = "No wiki link provided. Sorry!";
    private static final String DEFAULT_SOURCE = "No source book provided. Sorry!";
    private static final String DEFAULT_INFO = "No information provided. Sorry!";

    /**
     * File name constants
     */
    private final static String extensionName = "Database/";
    private final static String fileNameClasses = "Database/Classes/classes.json";
    private final static String fileNameBackgrounds = "Database/Backgrounds/backgrounds.json";
    private final static String fileNameRaces = "Database/Races/races.json";

    private final static String arrayNameClasses = "class";
    private final static String arrayNameBackgrounds = "background";
    private final static String arrayNameRaces = "race";

    // ----- SUPPORTED -----

    public static List<String> getSupportedClasses() {
        return getSupported(fileNameClasses, arrayNameClasses);
    }

    public static List<String> getSupportedRaces() {
        return getSupported(fileNameRaces, arrayNameRaces);
    }

    public static List<String> getSupportedBackgrounds() {
        return getSupported(fileNameBackgrounds, arrayNameBackgrounds);
    }

    private static List<String> getSupported(String fileName, String arrayName) {
        JSONObject object = FileUtilities.getJSONFileObject(fileName);
        assert object != null;
        JSONObject innerObject = object.getJSONObject("class");
        List<String> supported = new ArrayList<>();

        Iterator<String> elementNames = innerObject.keys();

        while (elementNames.hasNext()) {
            String element = elementNames.next();
            if (FileUtilities.getValueByKey(fileName, element, arrayName).equals("supported")) {
                supported.add(element);
            }
        }

        return supported;
    }

    // ----- CLASSES -----

    public static EmbedBuilder getClassByName(String classToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String classValue = FileUtilities.getValueByKey(fileNameClasses, classToFind, arrayNameClasses);

        if (classValue.equals("supported")) {
            addClassValues(embed, classToFind);
        }
        else embed.addField(classToFind, classValue, false);

        return embed;
    }

    private static void addClassValues(EmbedBuilder embed, String classToFind) {
        String directory = extensionName + "Classes/" + classToFind.substring(0, 1).toUpperCase() + classToFind.substring(1).toLowerCase() + "/" + classToFind + ".json";

        String name = FileUtilities.getValueByKey(directory, "name", classToFind);
        String wikiLink = FileUtilities.getValueByKey(directory, "wiki link", classToFind);
        String sourceBook = FileUtilities.getValueByKey(directory, "source book", classToFind);
        String hitDice = FileUtilities.getValueByKey(directory, "hit dice", classToFind);
        String hitPoints = FileUtilities.getValueByKey(directory, "hit points", classToFind);

        String armorProf = FileUtilities.getValueByKey(directory, "armor proficiencies", classToFind);
        String weaponProf = FileUtilities.getValueByKey(directory, "weapon proficiencies", classToFind);
        String toolProf = FileUtilities.getValueByKey(directory, "tool proficiencies", classToFind);
        String savingThrows = FileUtilities.getValueByKey(directory, "saving throw proficiencies", classToFind);
        String skillProf = FileUtilities.getValueByKey(directory, "skill proficiencies", classToFind);

        String equipment = FileUtilities.getValueByKey(directory, "equipment", classToFind);
        String subClasses = FileUtilities.getValueByKey(directory, "subclasses", classToFind);
        //String featuresList = FileUtilities.getValueByKey(directory, "features", classToFind);

        String abilityIncrease = "4th, 8th, 12th, 16th, and 19th levels. " +
                                 "Increase one ability score of your choice by 2, two ability scores of your choice by 1. " +
                                 "Cannot exceed 20.";

        // Null checks for the important bits to just define them a default value
        // Which is just name and wiki link, everything else can be skipped if null.
        if (name == null) name = DEFAULT_NAME;
        if (wikiLink == null) wikiLink = DEFAULT_WIKI;
        if (sourceBook == null) sourceBook = DEFAULT_SOURCE;

        // Build embed, finally
        embed.setTitle(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " (Source: " + sourceBook + ")");
        embed.addField("Wiki Link", wikiLink, false);

        // Hit dice
        if (hitDice != null) {
            embed.addField("Hit Dice", hitDice, false);
        }
        // Hit points
        if (hitPoints != null) {
            embed.addField("Hit Points", hitPoints, false);
        }

        // Here we start lists
        if (armorProf != null) { // armor
            embed.addField( "Armor Proficiencies", armorProf, true);
        }
        if (weaponProf != null) { // weapons
            embed.addField( "Weapon Proficiencies", weaponProf, true);
        }
        if (toolProf != null) { // tools
            embed.addField( "Tool Proficiencies", toolProf, true);
        }
        if (skillProf != null) { // skills
            embed.addField( "Skill Proficiencies", skillProf, true);
        }
        if (savingThrows != null) { // saving throws
            embed.addField( "Saving Throws Proficiencies", savingThrows, true);
        }
        if (equipment != null) { // equipment
            embed.addField( "Equipment", equipment, true);
        }
        if (subClasses != null) { // sub classes
            embed.addField("Sub Classes", subClasses, true);
        }
        /*
        if (featuresList != null) { // features
            StringBuilder listFull = new StringBuilder();
            for (Feature listItem : featuresList) {
                String temp = listItem.getName();
                listFull.append(temp).append("\n");
            }
            embed.addField("Features", listFull.toString(), true);
        }*/
        // TODO features

        embed.addField("Ability Score Increases", abilityIncrease, false);
    }

    // ----- RACES -----

    public static EmbedBuilder getRaceByName(String raceToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String raceValue = FileUtilities.getValueByKey(fileNameRaces, raceToFind, arrayNameRaces);

        if (raceValue.equals("supported")) {
            addRaceValues(embed, raceToFind);
        }
        else embed.addField(raceToFind, raceValue, false);

        return embed;
    }

    private static void addRaceValues(EmbedBuilder embed, String raceToFind) {

    }

    // ----- BACKGROUNDS -----

    public static EmbedBuilder getBackgroundByName(String backgroundToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String backgroundValue = FileUtilities.getValueByKey(fileNameBackgrounds, backgroundToFind, arrayNameBackgrounds);

        if (backgroundValue.equals("supported")) {
            addBackgroundValues(embed, backgroundToFind);
        }
        else embed.addField(backgroundToFind, backgroundValue, false);

        return embed;
    }

    private static void addBackgroundValues(EmbedBuilder embed, String backgroundToFind) {
        String directory = extensionName + "Backgrounds/" + backgroundToFind.substring(0, 1).toUpperCase() + backgroundToFind.substring(1).toLowerCase() + "/" + backgroundToFind + ".json";

        String name = FileUtilities.getValueByKey(directory, "name", backgroundToFind);
        String wikiLink = FileUtilities.getValueByKey(directory, "wiki link", backgroundToFind);

        String age = FileUtilities.getValueByKey(directory, "age", backgroundToFind);
        String size = FileUtilities.getValueByKey(directory, "size", backgroundToFind);
        String alignment = FileUtilities.getValueByKey(directory, "alignment", backgroundToFind);
        String speed = FileUtilities.getValueByKey(directory, "speed", backgroundToFind);

        String skillProf = FileUtilities.getValueByKey(directory, "skill proficiencies", backgroundToFind);
        String toolProf = FileUtilities.getValueByKey(directory, "tool proficiencies", backgroundToFind);
        String languages = FileUtilities.getValueByKey(directory, "languages", backgroundToFind);

        String abilityScoreImprovement = FileUtilities.getValueByKey(directory, "ability score improvement", backgroundToFind);
        String subRaces = FileUtilities.getValueByKey(directory, "subraces", backgroundToFind);
    }

    // ----- FEATURES -----

    public static EmbedBuilder getFeatureByName(String featureToFind, String classFrom) {
        EmbedBuilder embed = new EmbedBuilder();

        addFeatureValues(embed, featureToFind, classFrom);

        return embed;
    }

    public static EmbedBuilder listClassFeatures(String classFrom) {
        String directory = extensionName + "Classes/" + classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase() + "/" + classFrom + ".json";
        String features;

        EmbedBuilder embed = new EmbedBuilder();
        MessageUtilities.addEmbedDefaults(embed);

        if (FileUtilities.checkIfFileExists(directory)) {
            features = FileUtilities.getValueByKey(directory, "features", classFrom.toLowerCase());

            embed.setTitle(classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase());
            embed.addField("Features", features, false);
        }
        else {
            embed.addField("", "The provided class **" + classFrom + "** does not exist.", false);
        }

        return embed;
    }

    private static void addFeatureValues(EmbedBuilder embed, String featureToFind, String classFrom) {
        String directory = extensionName + "Classes/" + classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase() + "/" + classFrom.toLowerCase() + "features.json";

        String name = FileUtilities.getValueByKey(directory, "name", featureToFind.toLowerCase());
        String info = FileUtilities.getValueByKey(directory, "info", featureToFind.toLowerCase());
        String level = FileUtilities.getValueByKey(directory, "level", featureToFind.toLowerCase());

        if (name == null) name = DEFAULT_NAME;
        if (info == null) info = DEFAULT_INFO;
        if (level == null) level = "Not Provided";

        info = info.replace("\n", "");

        embed.setTitle(name + " (Level: " + level + ")");

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(info);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            embed.addField("", info.substring(start,end), false);
        }
    }
}
