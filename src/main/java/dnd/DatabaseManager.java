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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseManager {

    /**
     * Defaults
     */
    private static final String DEFAULT_NAME = "No name provided. Sorry!";
    private static final String DEFAULT_WIKI = "No wiki link provided. Sorry!";

    /**
     * File name constants
     */
    private final static String extensionName = "Database/";
    private final static String fileNameClasses = "Database/Classes/classes.json";
    private final static String fileNameFeatures = "Database/Features/features.json";
    private final static String fileNameBackgrounds = "Database/Backgrounds/backgrounds.json";
    private final static String fileNameRaces = "Database/Races/races.json";

    private final static String arrayNameClasses = "class";
    private final static String arrayNameFeatures = "feature";
    private final static String arrayNameBackgrounds = "background";
    private final static String arrayNameRaces = "race";

    private boolean databaseIsUsable() {
        return false;
    }

    public static EmbedBuilder getClassByName(String classToFind) {
        EmbedBuilder embed = new EmbedBuilder();
        String classValue = FileUtilities.getValueByKey(fileNameClasses, classToFind, arrayNameClasses);

        if (classValue.equals("supported")) {
            addClassValues(embed, classToFind);
        }
        else embed.addField(classToFind, classValue, false);

        return embed;
    }

    public static List<String> getSupportedClasses() {
        JSONObject object = FileUtilities.getJSONFileObject(fileNameClasses);
        assert object != null;
        JSONObject innerObject = object.getJSONObject("class");
        List<String> supported = new ArrayList<>();

        Iterator<String> elementNames = innerObject.keys();

        while (elementNames.hasNext()) {
            String element = elementNames.next();
            if (FileUtilities.getValueByKey(fileNameClasses, element, arrayNameClasses).equals("supported")) {
                supported.add(element);
            }
            System.out.println(element);
        }

        return supported;
    }

    public static void addClassValues(EmbedBuilder embed, String classToFind) {
        String name = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "name", classToFind);
        String wikiLink = FileUtilities.getValueByKey(extensionName + "/Classes/" + classToFind + ".json", "wiki link", classToFind);
        String hitDice = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "hit dice", classToFind);
        String hitPoints = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "hit points", classToFind);

        String armorProf = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "armor proficiencies", classToFind);
        String weaponProf = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "weapon proficiencies", classToFind);
        String toolProf = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "tool proficiencies", classToFind);
        String savingThrows = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "saving throw proficiencies", classToFind);
        String skillProf = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "skill proficiencies", classToFind);

        String equipment = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "equipment", classToFind);
        String subClasses = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "subclasses", classToFind);
        //String featuresList = FileUtilities.getValueByKey(extensionName + "Classes/" + classToFind + ".json", "features", classToFind);

        String abilityIncrease = "4th, 8th, 12th, 16th, and 19th levels. " +
                                 "Increase one ability score of your choice by 2, two ability scores of your choice by 1. " +
                                 "Cannot exceed 20.";

        // Null checks for the important bits to just define them a default value
        // Which is just name and wiki link, everything else can be skipped if null.
        if (name == null) name = DEFAULT_NAME;
        if (wikiLink == null) wikiLink = DEFAULT_WIKI;

        // Build embed, finally
        embed.setTitle(name);
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
}
