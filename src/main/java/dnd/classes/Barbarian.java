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
package dnd.classes;

import dnd.DNDConstants;
import dnd.other.features.BarbarianFeatures;
import dnd.other.features.Feature;

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
        );
    }

    @Override
    public List<String> getWeaponProficiencies() {
        return Arrays.asList(
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
                "Choose two from: "
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
