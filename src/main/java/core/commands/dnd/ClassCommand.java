package core.commands.dnd;

import core.commands.Command;
import dndinfo.classes.Class;
import dndinfo.other.Features.Feature;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.EmbedUtils;
import utils.Logger;

import java.awt.*;
import java.util.Arrays;
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
        Logger.info("CLASS");

        // Bypass sending message if it is already in a private message.
        if(!mre.isFromType(ChannelType.PRIVATE)) {
            // Send help message
            mre.getTextChannel().sendMessage(new MessageBuilder()
                    .append("Hey, ")
                    .append(mre.getAuthor())
                    .append(": Help information was sent as a private message.")
                    .build()).queue();
        }
        // Send help message
        sendPrivateMessage(mre.getAuthor().openPrivateChannel().complete(), args);

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("class", "classlist");
    }

    @Override
    public boolean isDND() {
        return true;
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
        return null;
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

            EmbedUtils.addDefaults(embed);

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

            EmbedUtils.addDefaults(embed);

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
            // TODO doesNotExist
        }
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

    }

    private void addEach (EmbedBuilder embed, String name, List<String> list, boolean inLine) {
        StringBuilder listFull = new StringBuilder();

        for (String listItem : list) {
            listFull.append(listItem).append("\n");
        }

        embed.addField(name, listFull.toString(), inLine);
    }
}
