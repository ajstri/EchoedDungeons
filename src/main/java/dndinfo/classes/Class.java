package dndinfo.classes;

import dndinfo.other.Features.Feature;

import java.util.List;

public abstract class Class {

    public abstract String getName();
    public abstract String getWikiLink();

    public abstract String getHitDice();
    public abstract List<String> getHitPoints();

    public abstract List<String> getArmorProficiencies();
    public abstract List<String> getWeaponProficiencies();
    public abstract List<String> getToolProficiencies();
    public abstract List<String> getSavingThrowProficiencies();
    public abstract List<String> getSkillProficiencies();

    public abstract List<String> getEquipment();
    public abstract List<String> getSubClasses();
    public abstract List<Feature> getFeaturesList();

    public String getAbilityScoreIncrease() {
        return "4th, 8th, 12th, 16th, and 19th levels. " +
                "Increase one ability score of your choice by 2, two ability scores of your choice by 1. " +
                "Cannot exceed 20.";
    }

}
