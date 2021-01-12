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

import echoedcore.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import echoedcore.utilities.FileUtilities;

import java.text.BreakIterator;
import java.util.*;

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
    private final static String fileNameClasses = "Database/Classes/classes.json";
    private final static String fileNameBackgrounds = "Database/Backgrounds/backgrounds.json";
    private final static String fileNameRaces = "Database/Races/races.json";
    private final static String fileNameLanguages = "Database/Languages/languages.json";

    private final static String arrayNameClasses = "class";
    private final static String arrayNameSubclasses = "subclass";
    private final static String arrayNameBackgrounds = "background";
    private final static String arrayNameRaces = "race";
    private final static String arrayNameLanguages = "language";

    // ----- SUPPORTED -----

    /**
     * Returns a list of supported classes.
     * @return a list of supported classes
     */
    public static List<String> getSupportedClasses() {
        return getSupported(fileNameClasses, arrayNameClasses);
    }

    /**
     * Returns a list of supported subclasses by class.
     * @return a list of supported subclasses
     */
    public static List<String> getSupportedSubclassesByClass(String className) {
        String directory = "Database/Classes/" + className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase() + "/Subclasses/" + className + "subclasses.json";
        return getSupported(directory, arrayNameSubclasses);
    }

    /**
     * Returns a list of supported races.
     * @return a list of supported races
     */
    public static List<String> getSupportedRaces() {
        return getSupported(fileNameRaces, arrayNameRaces);
    }

    /**
     * Returns a list of supported backgrounds.
     * @return a list of supported backgrounds
     */
    public static List<String> getSupportedBackgrounds() {
        return getSupported(fileNameBackgrounds, arrayNameBackgrounds);
    }

    /**
     * Returns a list of supported features for a given class.
     * @param className class to pull features from
     * @return a list of supported features
     */
    public static List<String> getSupportedFeaturesByClass(String className) {
        String directory = "Database/Classes/" + className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase() + "/" + className + "features.json";

        JSONObject object = FileUtilities.getJSONFileObject(directory);
        assert object != null;
        Set<String> supported = object.keySet();

        return new ArrayList<>(supported);
    }

    /**
     * Returns a list of supported features for a given subclass.
     * @param subclassName class to pull features from
     * @return a list of supported features
     */
    public static List<String> getSupportedFeaturesBySubclass(String className, String subclassName) {
        String directory = "Database/Classes/" + className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase() + "/Subclasses/" + subclassName.substring(0, 1).toUpperCase() + subclassName.substring(1).toLowerCase() + "/" + subclassName + "features.json";

        JSONObject object = FileUtilities.getJSONFileObject(directory);
        assert object != null;
        Set<String> supported = object.keySet();

        return new ArrayList<>(supported);
    }

    /**
     * Returns a list of supported languages.
     * @return a list of supported languages
     */
    public static List<String> getSupportedLanguages() {
        return getSupported(fileNameLanguages, arrayNameLanguages);
    }

    /**
     * Returns a list of supported items based on the given file and array name.
     * @param fileName file name to check for supported items
     * @param arrayName array name to check for supported items
     * @return a list of supported items
     */
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

    /**
     * Builds an embed with a class and it's information.
     * @param classToFind class to build embed for
     * @return embed of a class's information
     */
    public static EmbedBuilder getClassByName(String classToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String classValue = FileUtilities.getValueByKey(fileNameClasses, classToFind, arrayNameClasses);

        if (classValue.equals("supported")) {
            addClassValues(embed, classToFind);
        }
        else embed.addField(classToFind, classValue, false);

        return embed;
    }

    /**
     * Adds a class's information to an embed.
     * @param embed embed to add information to
     * @param classToFind class to add information of
     */
    private static void addClassValues(EmbedBuilder embed, String classToFind) {
        String directory = "Database/Classes/" + classToFind.substring(0, 1).toUpperCase() + classToFind.substring(1).toLowerCase() + "/" + classToFind + ".json";

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
        String subclassLevel = FileUtilities.getValueByKey(directory, "subclass level", classToFind);

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
        if (subClasses != null && subclassLevel != null) { // sub classes
            embed.addField("Sub Classes (Level Unlocked: " + subclassLevel + ")", subClasses, true);
        }
        if (featuresList != null) { // features
            embed.addField("Features", featuresList, false);
        }

        embed.addField("Ability Score Increases", abilityIncrease, false);
    }

    // ----- RACES -----

    /**
     * Builds an embed with a race and it's information.
     * @param raceToFind race to build embed for
     * @return embed of a race's information
     */
    public static EmbedBuilder getRaceByName(String raceToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String raceValue = FileUtilities.getValueByKey(fileNameRaces, raceToFind, arrayNameRaces);

        if (raceValue.equals("supported")) {
            addRaceValues(embed, raceToFind);
        }
        else embed.addField(raceToFind, raceValue, false);

        return embed;
    }

    /**
     * Adds a race's information to a given embed
     * @param embed embed to add information to
     * @param raceToFind race to add
     */
    private static void addRaceValues(EmbedBuilder embed, String raceToFind) {
        // TODO addRaceValues
    }

    // ----- BACKGROUNDS -----

    /**
     * Builds an embed with a background and it's information.
     * @param backgroundToFind background to build embed for
     * @return embed of a background's information
     */
    public static EmbedBuilder getBackgroundByName(String backgroundToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String backgroundValue = FileUtilities.getValueByKey(fileNameBackgrounds, backgroundToFind, arrayNameBackgrounds);

        if (backgroundValue.equals("supported")) {
            addBackgroundValues(embed, backgroundToFind);
        }
        else embed.addField(backgroundToFind, backgroundValue, false);

        return embed;
    }

    /**
     * Adds a background's information to a given embed
     * @param embed embed to add information to
     * @param backgroundToFind background to add
     */
    private static void addBackgroundValues(EmbedBuilder embed, String backgroundToFind) {
        String directory = "Database/Backgrounds/" + backgroundToFind.substring(0, 1).toUpperCase() + backgroundToFind.substring(1).toLowerCase() + "/" + backgroundToFind + ".json";

        String name = FileUtilities.getValueByKey(directory, "name", backgroundToFind);
        String wikiLink = FileUtilities.getValueByKey(directory, "wiki link", backgroundToFind);
        String sourceBook = FileUtilities.getValueByKey(directory, "source book", backgroundToFind);

        String skillProf = FileUtilities.getValueByKey(directory, "skill proficiencies", backgroundToFind);
        String toolProf = FileUtilities.getValueByKey(directory, "tool proficiencies", backgroundToFind);
        String languages = FileUtilities.getValueByKey(directory, "languages", backgroundToFind);

        String equipment = FileUtilities.getValueByKey(directory, "equipment", backgroundToFind);
        String features = FileUtilities.getValueByKey(directory, "features", backgroundToFind);
        String variants = FileUtilities.getValueByKey(directory, "variants", backgroundToFind);

        addDefaults(name, wikiLink, sourceBook, embed);

        if (skillProf != null) { // skills
            embed.addField( "Skill Proficiencies", skillProf, true);
        }
        if (toolProf != null) { // tools
            embed.addField( "Tool Proficiencies", toolProf, true);
        }
        if (languages != null) { // languages
            embed.addField( "Languages", languages, true);
        }
        if (equipment != null) { // equipment
            embed.addField( "Equipment", equipment, true);
        }
        if (features != null) { // features
            embed.addField( "Features", features, true);
        }
        if (variants != null) { // variants
            embed.addField( "Variants", variants, true);
        }
    }

    // ----- FEATURES -----

    /**
     * Builds an embed with a feature and it's information.
     * @param featureToFind feature to build embed for
     * @param classFrom class the feature is from
     * @return embed of a feature's information
     */
    public static EmbedBuilder getClassFeatureByName(String featureToFind, String classFrom) {
        EmbedBuilder embed = new EmbedBuilder();

        addClassFeatureValues(embed, featureToFind, classFrom);

        return embed;
    }

    /**
     * Lists all features from a class.
     * @param classFrom class to list features for
     * @return list of features
     */
    public static EmbedBuilder listClassFeatures(String classFrom) {
        String directory = "Database/Classes/" + classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase() + "/" + classFrom + ".json";
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

    /**
     * Adds a feature's information to a given embed
     * @param embed embed to add information to
     * @param featureToFind feature to add
     * @param classFrom class the feature is from
     */
    private static void addClassFeatureValues(EmbedBuilder embed, String featureToFind, String classFrom) {
        String directory = "Database/Classes/" + classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase() + "/" + classFrom.toLowerCase() + "features.json";

        addFeatureValues(embed, featureToFind, classFrom, directory);
    }

    /**
     * Builds an embed with a feature and it's information.
     * @param featureToFind feature to build embed for
     * @param subclassFrom class the feature is from
     * @return embed of a feature's information
     */
    public static EmbedBuilder getSubclassFeatureByName(String featureToFind, String subclassFrom, String classFrom) {
        EmbedBuilder embed = new EmbedBuilder();

        addSubclassFeatureValues(embed, featureToFind, subclassFrom, classFrom);

        return embed;
    }

    /**
     * Adds a feature's information to a given embed
     * @param embed embed to add information to
     * @param featureToFind feature to add
     * @param subclassFrom class the feature is from
     */
    private static void addSubclassFeatureValues(EmbedBuilder embed, String featureToFind, String subclassFrom, String classFrom) {
        String directory = "Database/Classes/"
                + classFrom.substring(0, 1).toUpperCase() + classFrom.substring(1).toLowerCase()
                + "/Subclasses/" + subclassFrom.substring(0, 1).toUpperCase() + subclassFrom.substring(1).toLowerCase()
                + "/" + subclassFrom.toLowerCase() + "features.json";

        addFeatureValues(embed, featureToFind, subclassFrom, directory);
    }

    /**
     * Adds a feature to an embed
     * @param embed embed to add to
     * @param featureToFind feature to add
     * @param subclassFrom class or subclass the feature is from
     * @param directory directory of the file
     */
    private static void addFeatureValues(EmbedBuilder embed, String featureToFind, String subclassFrom, String directory) {
        String name = FileUtilities.getValueByKey(directory, "name", featureToFind.toLowerCase());
        String info = FileUtilities.getValueByKey(directory, "info", featureToFind.toLowerCase());
        String level = FileUtilities.getValueByKey(directory, "level", featureToFind.toLowerCase());

        if (name == null) name = DEFAULT_NAME;
        if (info == null) info = DEFAULT_INFO;
        if (level == null) level = DEFAULT_LEVEL;

        info = info.replace("\n", "");

        embed.setTitle(name + " (Level: " + level + ", Class: " + subclassFrom.substring(0, 1).toUpperCase() + subclassFrom.substring(1).toLowerCase() + ")");

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

    /**
     * Builds an embed with a language and it's information.
     * @param languageName language to build embed for
     * @return embed of a language's information
     */
    public static EmbedBuilder getLanguageByName(String languageName) {
        EmbedBuilder embed = new EmbedBuilder();
        String languageValue = FileUtilities.getValueByKey(fileNameLanguages, languageName, arrayNameLanguages);

        if (languageValue.equals("supported")) {
            addLanguageValues(embed, languageName);
        }
        else embed.addField(languageName, languageValue, false);

        return embed;
    }

    /**
     * Lists all supported languages.
     * @return embed with all supported languages
     */
    public static EmbedBuilder listAllLanguages() {
        EmbedBuilder embed = new EmbedBuilder();
        MessageUtilities.addEmbedDefaults(embed);
        embed.setTitle("Languages Supported");
        for (String language: getSupported(fileNameLanguages, arrayNameLanguages)) {
            embed.addField("", language.substring(0, 1).toUpperCase() + language.substring(1).toLowerCase(), true);
        }
        return embed;
    }

    /**
     * Adds a language's information to a given embed
     * @param embed embed to add information to
     * @param language language to add
     */
    private static void addLanguageValues(EmbedBuilder embed, String language) {
        String directory = "Database/Languages/" + language.toLowerCase() + ".json";

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

    /**
     * Add default information for embeds.
     * @param name name of item
     * @param wikiLink wikidot link of item
     * @param sourceBook sourcebook of item
     * @param embed embed to add information to
     */
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

    /**
     * Adds basic information about a given object.
     * @param embed embed to add to
     * @param supported supported object to add information of
     * @param directory directory to pull information from
     */
    public static void addBasicInformation(EmbedBuilder embed, String supported, String directory) {
        String name;
        String wikiLink;
        if (FileUtilities.checkIfFileExists(directory)) {
            name = FileUtilities.getValueByKey(directory, "name", supported);
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            wikiLink = FileUtilities.getValueByKey(directory, "wiki link", supported);
            embed.addField(name, wikiLink, false);
        }
    }
}
