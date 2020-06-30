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
package dnd.other.features;

import core.commands.dnd.FeatureCommand;
import dnd.DNDConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BarbarianFeatures {

    public BarbarianFeatures(FeatureCommand features) {
        features.registerFeature(new UnarmoredDefense());
        features.registerFeature(new Rage());
        features.registerFeature(new RecklessAttack());
        features.registerFeature(new DangerSense());
        features.registerFeature(new PrimalPath());
        features.registerFeature(new ExtraAttack());
        features.registerFeature(new FastMovement());
        features.registerFeature(new FeralInstinct());
        features.registerFeature(new BrutalCritical());
        features.registerFeature(new RelentlessRage());
        features.registerFeature(new PersistentRage());
        features.registerFeature(new IndomitableMight());
        features.registerFeature(new PrimalChampion());
    }

    public static class Rage extends Feature {
        @Override
        public String getName() {
            return "Rage";
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "In battle, you fight with primal ferocity. On your turn, you can enter a rage as a bonus action.\n",
                    "While raging, you gain the following benefits if you aren’t wearing heavy armor:\n",
                    "You have advantage on Strength checks and Strength saving throws.\n" +
                    "When you make a melee weapon attack using Strength, you gain a bonus to the damage roll that increases as you gain levels as a barbarian, as shown in the Rage Damage column of the Barbarian table.\n",
                    "You have resistance to bludgeoning, piercing, and slashing damage.\n",
                    "If you are able to cast spells, you can’t cast them or concentrate on them while raging.\n",
                    "Your rage lasts for 1 minute. It ends early if you are knocked unconscious or if your turn ends, and you haven’t attacked a hostile creature since your last turn or taken damage since then. You can also end your rage on your turn as a bonus action.\n",
                    "Once you have raged the number of times shown for your barbarian level in the Rages' column of the Barbarian table, you must finish a long rest before you can rage again."
            );
        }
    }
    public static class UnarmoredDefense extends Feature {
        @Override
        public String getName() {
            return "Unarmored Defense";
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public List<String> getInfo() {
            return Collections.singletonList(
                    "While you are not wearing any armor, " +
                    "your Armor Class equals 10 + your " + DNDConstants.DEX_MOD + " plus your " + DNDConstants.CON_MOD +
                    "You can use a shield and still gain this benefit."
            );
        }
    }
    public static class RecklessAttack extends Feature {
        @Override
        public String getName() {
            return "Reckless Attack";
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "Starting at 2nd level, you can throw aside all concern for defense to attack with fierce desperation. ",
                    "When you make your first attack on your turn, you can decide to attack recklessly. ",
                    "Doing so gives you advantage on melee weapon attack rolls using Strength during this turn, but attack rolls against you have advantage until your next turn."
            );
        }
    }
    public static class DangerSense extends Feature {

        @Override
        public String getName() {
            return "Danger Sense";
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "At 2nd level, you gain an uncanny sense of when things nearby aren’t as they should be, giving you an edge when you dodge away from danger.",
                    "You have advantage on Dexterity saving throws against effects that you can see, such as traps and spells. To gain this benefit, you can’t be blinded, deafened, or incapacitated."
            );
        }
    }
    public static class PrimalPath extends Feature {

        @Override
        public String getName() {
            return "Primal Path";
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "At 3rd level, you choose a path that shapes the nature of your rage. ",
                    "The Path of the Berserker is detailed at the end of the class description, and additional primal paths are available in other sources. ",
                    "Your choice grants you features at 3rd level and again at 6th, 10th, and 14th levels."
            );
        }
    }
    public static class ExtraAttack extends Feature {

        @Override
        public String getName() {
            return "Extra Attack";
        }

        @Override
        public int getLevel() {
            return 5;
        }

        @Override
        public List<String> getInfo() {
            return Collections.singletonList(
                    "Beginning at 5th level, you can attack twice, instead of once, whenever you take the Attack action on your turn."
            );
        }
    }
    public static class FastMovement extends Feature {

        @Override
        public String getName() {
            return "Fast Movement";
        }

        @Override
        public int getLevel() {
            return 5;
        }

        @Override
        public List<String> getInfo() {
            return Collections.singletonList(
                    "Starting at 5th level, your speed increases by 10 feet while you aren’t wearing heavy armor."
            );
        }
    }
    public static class FeralInstinct extends Feature {

        @Override
        public String getName() {
            return "Feral Instinct";
        }

        @Override
        public int getLevel() {
            return 7;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "By 7th level, your instincts are so honed that you have advantage on initiative rolls.\n",
                    "Additionally, if you are surprised at the beginning of combat and aren’t incapacitated, " +
                    "you can act normally on your first turn, but only if you enter your rage before doing anything else on that turn."
            );
        }
    }
    public static class BrutalCritical extends Feature {

        @Override
        public String getName() {
            return "Brutal Critical";
        }

        @Override
        public int getLevel() {
            return 9;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "Beginning at 9th level, you can roll one additional weapon damage die when determining the extra damage for a critical hit with a melee attack.\n",
                    "At 13th level, you can roll two additional weapon damage dice when determining the extra damage for a critical hit with a melee attack.\n",
                    "At 17th level, you can roll three additional weapon damage dice when determining the extra damage for a critical hit with a melee attack."
            );
        }
    }
    public static class RelentlessRage extends Feature {

        @Override
        public String getName() {
            return "Relentless Rage";
        }

        @Override
        public int getLevel() {
            return 11;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "Starting at 11th level, your rage can keep you fighting despite grievous wounds. ",
                    "If you drop to 0 hit points while you’re raging and don’t die outright, you can make a DC 10 Constitution saving throw. ",
                    "If you succeed, you drop to 1 hit point instead.\n",
                    "Each time you use this feature after the first, the DC increases by 5. When you finish a short or long rest, the DC resets to 10."
            );
        }
    }
    public static class PersistentRage extends Feature {

        @Override
        public String getName() {
            return "Persistent Rage";
        }

        @Override
        public int getLevel() {
            return 15;
        }

        @Override
        public List<String> getInfo() {
            return Collections.singletonList(
                    "Beginning at 15th level, your rage is so fierce it ends early only if you fall unconscious or if you choose to end it."
            );
        }
    }
    public static class IndomitableMight extends Feature {

        @Override
        public String getName() {
            return "Indomitable Might";
        }

        @Override
        public int getLevel() {
            return 18;
        }

        @Override
        public List<String> getInfo() {
            return Collections.singletonList(
                    "Beginning at 18th level, if your total for a Strength check is less than your Strength score, you can use that score in place of the total."
            );
        }
    }
    public static class PrimalChampion extends Feature {

        @Override
        public String getName() {
            return "Primal Champion";
        }

        @Override
        public int getLevel() {
            return 20;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "At 20th level, you embody the power of the wilds. Your Strength and Constitution scores increase by 4. ",
                    "Your maximum for those scores is now 24."
            );
        }
    }
}