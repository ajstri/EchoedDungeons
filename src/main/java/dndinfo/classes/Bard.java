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

import dndinfo.other.DNDConstants;
import dndinfo.other.features.Feature;
import dndinfo.other.proficiencylists.Proficiencies;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bard extends Class {
    @Override
    public String getName() {
        return "Bard";
    }

    @Override
    public String getWikiLink() {
        return "http://dnd5e.wikidot.com/bard";
    }

    @Override
    public String getHitDice() {
        return "1d8 per Bard level";
    }

    @Override
    public List<String> getHitPoints() {
        return Arrays.asList(
                "First Level: 8 + " + DNDConstants.CON_MOD,
                "After First: 1d8 (or 5) + your " + DNDConstants.CON_MOD + " per Bard level"
        );
    }

    @Override
    public List<String> getArmorProficiencies() {
        return Collections.singletonList(
                Proficiencies.Armor.armor_light
        );
    }

    @Override
    public List<String> getWeaponProficiencies() {
        return Arrays.asList(
                Proficiencies.Weapons.weapons_simple,
                Proficiencies.Weapons.hand_crossbows,
                Proficiencies.Weapons.longswords,
                Proficiencies.Weapons.rapiers,
                Proficiencies.Weapons.shortswords
        );
    }

    @Override
    public List<String> getToolProficiencies() {
        return Collections.singletonList(
                "Three musical instruments"
        );
    }

    @Override
    public List<String> getSavingThrowProficiencies() {
        return Arrays.asList(
                DNDConstants.DEX,
                DNDConstants.CHA
        );
    }

    @Override
    public List<String> getSkillProficiencies() {
        return Collections.singletonList("Choose any three.");
    }

    @Override
    public List<String> getEquipment() {
        return Arrays.asList(
                "(a) a rapier, (b) a longsword, or (c) any simple weapon",
                "(a) a diplomat's pack or (b) an entertainer's pack",
                "(a) a lute or (b) any other musical instrument",
                "leather armor",
                "a dagger"
        );
    }

    @Override
    public List<String> getSubClasses() {
        return Arrays.asList(
                "Creation (UA)",
                "Eloquence (UA)",
                "Glamour",
                "Lore",
                "Satire (UA)",
                "Swords",
                "Valor",
                "Whispers"
        );
    }

    @Override
    public List<Feature> getFeaturesList() {
        return null;
    }
}
