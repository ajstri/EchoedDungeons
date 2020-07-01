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
package utilities.dnd;

import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import utilities.FileUtilities;
import utilities.MessageUtilities;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *  DatabaseManager class of the EchoedDungeons project
 *
 *  Includes methods to handle data provided in the Database directory.
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class DatabaseManager {

    /**
     * Defaults
     */
    private static final String DEFAULT_NAME = "No name provided. Sorry!";
    private static final String DEFAULT_WIKI = "No wiki link provided. Sorry!";
    private static final String DEFAULT_SOURCE = "No source book provided. Sorry!";
    private static final String DEFAULT_INFO = "No information provided. Sorry!";
    private static final String DEFAULT_LEVEL = "None Provided";
    private static final String DEFAULT_SCRIPT = "No script provided";
    private static final String DEFAULT_COMMON = "No common speakers provided";

    /**
     * File name constants
     */
    private final static String extensionName = "Database/";
    private final static String fileNameClasses = "Database/Classes/classes.json";
    private final static String fileNameBackgrounds = "Database/Backgrounds/backgrounds.json";
    private final static String fileNameRaces = "Database/Races/races.json";
    private final static String fileNameLanguages = "Database/Languages/languages.json";

    private final static String arrayNameClasses = "class";
    private final static String arrayNameBackgrounds = "background";
    private final static String arrayNameRaces = "race";
    private final static String arrayNameLanguages = "language";

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

    public static List<String> getSupportedLanguages() {
        return getSupported(fileNameLanguages, arrayNameLanguages);
    }

    private static List<String> getSupported(String fileName, String arrayName) {
        JSONObject object = FileUtilities.getJSONFileObject(fileName);
        assert object != null;
        JSONObject innerObject = object.getJSONObject(arrayName);
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
        String featuresList = FileUtilities.getValueByKey(directory, "features", classToFind);

        String abilityIncrease = "4th, 8th, 12th, 16th, and 19th levels. " +
                                 "Increase one ability score of your choice by 2, two ability scores of your choice by 1. " +
                                 "Cannot exceed 20.";

        addDefaults(name, wikiLink, sourceBook, embed);

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
        if (featuresList != null) { // features
            embed.addField("Features", featuresList, false);
        }

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
        String sourceBook = FileUtilities.getValueByKey(directory, "source book", backgroundToFind);

        String age = FileUtilities.getValueByKey(directory, "age", backgroundToFind);
        String size = FileUtilities.getValueByKey(directory, "size", backgroundToFind);
        String alignment = FileUtilities.getValueByKey(directory, "alignment", backgroundToFind);
        String speed = FileUtilities.getValueByKey(directory, "speed", backgroundToFind);

        String skillProf = FileUtilities.getValueByKey(directory, "skill proficiencies", backgroundToFind);
        String toolProf = FileUtilities.getValueByKey(directory, "tool proficiencies", backgroundToFind);
        String languages = FileUtilities.getValueByKey(directory, "languages", backgroundToFind);

        String abilityScoreImprovement = FileUtilities.getValueByKey(directory, "ability score improvement", backgroundToFind);
        String subRaces = FileUtilities.getValueByKey(directory, "subraces", backgroundToFind);

        addDefaults(name, wikiLink, sourceBook, embed);

        if (age != null) { // age
            embed.addField( "Age", age, true);
        }
        if (size != null) { // size
            embed.addField( "Size", size, true);
        }
        if (alignment != null) { // alignment
            embed.addField( "Alignment", alignment, true);
        }
        if (speed != null) { // speed
            embed.addField( "Speed", speed, true);
        }
        if (skillProf != null) { // skills
            embed.addField( "Skill Proficiencies", skillProf, true);
        }
        if (toolProf != null) { // tools
            embed.addField( "Tool Proficiencies", toolProf, true);
        }
        if (languages != null) { // languages
            embed.addField( "Languages", languages, true);
        }
        if (abilityScoreImprovement != null) { // ability score
            embed.addField( "Ability Score Improvement", abilityScoreImprovement, true);
        }
        if (subRaces != null) { // sub races
            embed.addField( "Subraces", subRaces, true);
        }
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
        if (level == null) level = DEFAULT_LEVEL;

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

    // ----- LANGUAGES -----

    public static EmbedBuilder getLanguageByName(String languageName) {
        EmbedBuilder embed = new EmbedBuilder();
        String languageValue = FileUtilities.getValueByKey(fileNameLanguages, languageName, arrayNameLanguages);

        if (languageValue.equals("supported")) {
            addLanguageValues(embed, languageName);
        }
        else embed.addField(languageName, languageValue, false);

        return embed;
    }

    public static EmbedBuilder listAllLanguages() {
        EmbedBuilder embed = new EmbedBuilder();
        MessageUtilities.addEmbedDefaults(embed);
        embed.setTitle("Languages Supported");
        for (String language: getSupported(fileNameLanguages, arrayNameLanguages)) {
            embed.addField("", language.substring(0, 1).toUpperCase() + language.substring(1).toLowerCase(), true);
        }
        return embed;
    }

    private static void addLanguageValues(EmbedBuilder embed, String language) {
        String directory = extensionName + "Languages/" + language.toLowerCase() + ".json";

        String name = FileUtilities.getValueByKey(directory, "name", language);
        String commonlySpoken = FileUtilities.getValueByKey(directory, "common speakers", language);
        String script = FileUtilities.getValueByKey(directory, "script", language);

        if (name == null) name = DEFAULT_NAME;
        if (commonlySpoken == null) commonlySpoken = DEFAULT_COMMON;
        if (script == null) script = DEFAULT_SCRIPT;

        embed.addField("Language", name, true);
        embed.addField("Commonly Spoken By", commonlySpoken, true);
        embed.addField("Script", script, true);
    }

    // ----- Other -----

    private static void addDefaults(String name, String wikiLink, String sourceBook, EmbedBuilder embed) {
        // Null checks for the important bits to just define them a default value
        // Which is just name and wiki link, everything else can be skipped if null.
        if (name == null) name = DEFAULT_NAME;
        if (wikiLink == null) wikiLink = DEFAULT_WIKI;
        if (sourceBook == null ) sourceBook = DEFAULT_SOURCE;

        // Build embed, finally
        embed.setTitle(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " (Source: " + sourceBook + ")");
        embed.addField("Wiki Link", wikiLink, false);
    }
}
