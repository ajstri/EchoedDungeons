package dndinfo.classes;

import dndinfo.other.DNDConstants;
import dndinfo.other.Features.Feature;
import dndinfo.other.proficiencylists.Proficiencies;

import java.util.Arrays;
import java.util.List;

public class Artificer extends Class {
    @Override
    public String getName() {
        return "Artificer";
    }

    @Override
    public String getWikiLink() {
        return "http://dnd5e.wikidot.com/artificer";
    }

    @Override
    public String getHitDice() {
        return "1d8 per Artificer level";
    }

    @Override
    public List<String> getHitPoints() {
        return Arrays.asList(
                "First Level: 8 " + DNDConstants.CON_MOD,
                "After First: 1d8 (or 5) + your " + DNDConstants.CON_MOD + " per Artificer level"
        );
    }

    @Override
    public List<String> getArmorProficiencies() {
        return Arrays.asList(
                Proficiencies.Armor.armor_light,
                Proficiencies.Armor.armor_medium,
                Proficiencies.Armor.shields
        );
    }

    @Override
    public List<String> getWeaponProficiencies() {
        return Arrays.asList(
                Proficiencies.Weapons.weapons_simple,
                Proficiencies.Weapons.hand_crossbows,
                Proficiencies.Weapons.heavy_crossbows
        );
    }

    @Override
    public List<String> getToolProficiencies() {
        return Arrays.asList(
                Proficiencies.Tools.thieves_tools,
                Proficiencies.Tools.tinkers_tools,
                Proficiencies.Tools.artisans_tools
        );
    }

    @Override
    public List<String> getSavingThrowProficiencies() {
        return Arrays.asList(
                DNDConstants.CON,
                DNDConstants.INT
        );
    }

    @Override
    public List<String> getSkillProficiencies() {
        return Arrays.asList(
                "Choose two from: ",
                Proficiencies.Skills.arcana,
                Proficiencies.Skills.history,
                Proficiencies.Skills.investigation,
                Proficiencies.Skills.medicine,
                Proficiencies.Skills.nature,
                Proficiencies.Skills.perception,
                Proficiencies.Skills.sleight
        );
    }

    @Override
    public List<String> getEquipment() {
        return Arrays.asList(
                "any two simple weapons",
                "a light crossbow and 20 bolts",
                "(a) studded leather armor or (b) scale mail",
                "thieves’ tools",
                "dungeoneer’s pack"
        );
    }

    @Override
    public List<String> getSubClasses() {
        return Arrays.asList(
                "Alchemist",
                "Armorer (UA)",
                "Artillerist",
                "Battle Smith"
        );
    }

    @Override
    public List<Feature> getFeaturesList() {
        return null;
    }
}
