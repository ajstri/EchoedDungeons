package dndinfo.classes;

import dndinfo.other.DNDConstants;
import dndinfo.other.features.BarbarianFeatures;
import dndinfo.other.features.Feature;
import dndinfo.other.proficiencylists.Proficiencies;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Barbarian extends Class {
    @Override
    public String getName() {
        return "Barbarian";
    }

    @Override
    public String getWikiLink() {
        return "http://dnd5e.wikidot.com/barbarian";
    }

    @Override
    public String getHitDice() {
        return "1d12 per Barbarian level";
    }

    @Override
    public List<String> getHitPoints() {
        return Arrays.asList(
                "First Level: 12 + " + DNDConstants.CON_MOD,
                "After First: 1d12 (or 7) + your " + DNDConstants.CON_MOD + " per Barbarian level"
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
                Proficiencies.Weapons.weapons_martial
        );
    }

    @Override
    public List<String> getToolProficiencies() {
        return Collections.singletonList(
                "None"
        );
    }

    @Override
    public List<String> getSavingThrowProficiencies() {
        return Arrays.asList(
                DNDConstants.STR, DNDConstants.CON
        );
    }

    @Override
    public List<String> getSkillProficiencies() {
        // Choose two from Animal Handling, Athletics, Intimidation, Nature, Perception, and Survival
        return Arrays.asList(
                "Choose two from: ",
                Proficiencies.Skills.animal_handling,
                Proficiencies.Skills.athletics,
                Proficiencies.Skills.intimidation,
                Proficiencies.Skills.nature,
                Proficiencies.Skills.perception,
                Proficiencies.Skills.survival
        );
    }

    @Override
    public List<String> getEquipment() {
        return Arrays.asList(
                "(a) a greataxe or (b) any martial melee weapon ",
                "(a) two handaxes or (b) any simple weapon",
                "An explorer’s pack and four javelins"
        );
    }

    @Override
    public List<String> getSubClasses() {
        return Arrays.asList(
                "Path of the Beast (UA)",
                "Path of the Berserker",
                "Path of the Wild Soul (UA)"
        );
    }

    @Override
    public List<Feature> getFeaturesList() {
        return Arrays.asList(
                new BarbarianFeatures.Rage(),
                new BarbarianFeatures.UnarmoredDefense(),
                new BarbarianFeatures.RecklessAttack(),
                new BarbarianFeatures.DangerSense(),
                new BarbarianFeatures.PrimalPath(),
                new BarbarianFeatures.ExtraAttack(),
                new BarbarianFeatures.FastMovement(),
                new BarbarianFeatures.FeralInstinct(),
                new BarbarianFeatures.BrutalCritical(),
                new BarbarianFeatures.RelentlessRage(),
                new BarbarianFeatures.PersistentRage(),
                new BarbarianFeatures.IndomitableMight(),
                new BarbarianFeatures.PrimalChampion()
        );
    }
}
