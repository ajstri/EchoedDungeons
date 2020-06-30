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
import dndinfo.classes.Class;
import dndinfo.other.features.Feature;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class ClassCommand extends Command {

    private final String DEFAULT_NAME = "No name provided. Sorry!";
    private final String DEFAULT_WIKI = "No wiki link provided. Sorry!";

    public final TreeMap<String, Class> classes;

    public ClassCommand() {
        classes = new TreeMap<>();
    }

    public void registerClass(Class classToRegister) {
        classes.put(classToRegister.getName(), classToRegister);
    }

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.
        Main.getLog().info("CLASS (called by " + mre.getAuthor().getAsTag() + ")");

        // Bypass sending message if it is already in a private message.
        MessageUtilities.sendIfNotPrivate(mre);
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("class", "classlist");
    }

    @Override
    public String getModule() {
        return Constants.DND;
    }

    @Override
    public String getDescription() {
        return "Lists all supported classes in EchoedDungeons' database.";
    }

    @Override
    public String getName() {
        return "Class Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "class [class name]`");
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }

    private void sendPrivateMessage (PrivateChannel channel, String[] args) {
        // If arg.length < 2 send classes list.
        // else find class in list.
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Classes Supported").setColor(Color.RED);

            MessageUtilities.addEmbedDefaults(embed);

            String name;
            String wikiLink;

            // For each command, add its values to embed.
            for (Class c : classes.values()) {
                if (c.getName() == null) {
                    name = DEFAULT_NAME;
                }
                else {
                    name = c.getName();
                }
                if (c.getWikiLink() == null) {
                    wikiLink = DEFAULT_WIKI;
                }
                else {
                    wikiLink = c.getWikiLink();
                }
                embed.addField(name, wikiLink, false);
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();

            MessageUtilities.addEmbedDefaults(embed);

            String command = args[1];
            // Check each command. If it is the command searched for, build embed.
            for (Class c : classes.values()) {
                if (c.getName().toLowerCase().contains(command)) {
                    // Define values.
                    addClassValues(embed, c);

                    // Send embed.
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }
            // If it reaches this point, it does not exist.
            doesNotExist(channel, args);
        }
    }

    /**
     * Sends a message telling the user their search doesn't exist
     * @param channel channel to send message
     * @param args arguments to build message
     */
    private static void doesNotExist(PrivateChannel channel, String[] args) {
        // If it reaches this point, the command searched for does not exist.
        channel.sendMessage(new MessageBuilder()
                .append("The provided class '**")
                .append(args[1])
                .append("**' does not exist. Use `")
                .append(Main.getConfig().getPrefix())
                .append("class` to list all classes.")
                .build()).queue();
    }

    private void addClassValues(EmbedBuilder embed, Class c) {
        String name = c.getName();
        String wikiLink = c.getWikiLink();
        String hitDice = c.getHitDice();
        List<String> hitPoints = c.getHitPoints();

        List<String> armorProf = c.getArmorProficiencies();
        List<String> weaponProf = c.getWeaponProficiencies();
        List<String> toolProf = c.getToolProficiencies();
        List<String> savingThrows = c.getSavingThrowProficiencies();
        List<String> skillProf = c.getSkillProficiencies();

        List<String> equipment = c.getEquipment();
        List<String> subClasses = c.getSubClasses();
        List<Feature> featuresList = c.getFeaturesList();

        String abilityIncrease = c.getAbilityScoreIncrease();

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
            addEach(embed,"Hit Points", hitPoints, false);
        }

        // Here we start lists
        if (armorProf != null) { // armor
            addEach(embed, "Armor Proficiencies", armorProf, true);
        }
        if (weaponProf != null) { // weapons
            addEach(embed, "Weapon Proficiencies", weaponProf, true);
        }
        if (toolProf != null) { // tools
            addEach(embed, "Tool Proficiencies", toolProf, true);
        }
        if (skillProf != null) { // skills
            addEach(embed, "Skill Proficiencies", skillProf, true);
        }
        if (savingThrows != null) { // saving throws
            addEach(embed, "Saving Throws Proficiencies", savingThrows, true);
        }
        if (equipment != null) { // equipment
            addEach(embed, "Equipment", equipment, true);
        }
        if (subClasses != null) { // sub classes
            addEach(embed, "Sub Classes", subClasses, true);
        }
        if (featuresList != null) { // features
            StringBuilder listFull = new StringBuilder();
            for (Feature listItem : featuresList) {
                String temp = listItem.getName();
                listFull.append(temp).append("\n");
            }
            embed.addField("Features", listFull.toString(), true);
        }

        embed.addField("Ability Score Increases", abilityIncrease, false);
    }

    private void addEach (EmbedBuilder embed, String name, List<String> list, boolean inLine) {
        StringBuilder listFull = new StringBuilder();

        for (String listItem : list) {
            listFull.append(listItem).append("\n");
        }

        embed.addField(name, listFull.toString(), inLine);
    }
}
