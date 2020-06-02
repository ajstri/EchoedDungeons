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
package dndinfo.classes;

import dndinfo.other.features.Feature;

import java.util.List;

public abstract class Class {

    // TODO javadoc

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
