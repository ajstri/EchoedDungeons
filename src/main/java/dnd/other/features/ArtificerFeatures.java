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

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class ArtificerFeatures { // TODO artificer features

    public ArtificerFeatures (FeatureCommand features) {
        features.registerFeature(new MagicalTinkering());
        features.registerFeature(new InfuseItem());
    }

    public static class MagicalTinkering extends Feature {

        @Override
        public String getName() {
            return "Magical Tinkering";
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "At 1st level, you learn how to invest a spark of magic in objects that would otherwise be mundane. To use this ability, you must tinker’s tools, or other artisan’s tools in hand. You then touch a Tiny non-magical object as an action and give it one of the following magical properties of your choice: ",
                    "The object sheds bright light in a 5-foot radius and dim light for an additional 5 feet.",
                    "Whenever tapped by a creature, the object emits a recorded message that can be heard up to 10 feet away. You utter the message when you bestow this property on the object, and the recording can be no more than 6 seconds long.",
                    "The object continuously emits your choice of an odor or a nonverbal sound (wind, waves, chirping, or the like). The chosen phenomenon is perceivable up to 10 feet away.",
                    "A static visual effect appears on one of the object’s surfaces. This effect can be a picture, up to 25 words of text, lines and shapes, or a mixture of these elements, as you like.",
                    "The chosen property lasts indefinitely. As an action, you can touch the object and end the property early. You can give the magic of this feature to multiple objects, touching one object each time you use the feature, and a single object can bear only one of the properties at a time. The maximum number of objects you can affect with the feature at one time is equal to your Intelligence modifier (minimum of one object). If you try to exceed your maximum, the oldest property immediately ends, and then the new property applies.");
        }
    }

    public static class InfuseItem extends Feature {

        @Override
        public String getName() {
            return "Infuse Item";
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public List<String> getInfo() {
            return Arrays.asList(
                    "Infuse Item: At 2nd level, you gain the ability to imbue mundane items with certain magical infusions. The magic items you create with this feature are effectively prototypes of permanent items.",
                    "Infusions Known: When you gain this feature, pick four artificer infusions to learn. You learn additional infusions of your choice when you reach certain levels in this class, as shown in the Infusions Known column of the Artificer table. Whenever you gain a level in this class, you can replace one of the artificer infusions you learned with a new one.",
                    "Infusing an Item: Whenever you finish a long rest, you can touch a non-magical object and imbue it with one of your artificer infusions, turning it into a magic item.",
                    "An infusion works on only certain kinds of objects, as specified in the infusion’s description. If the item requires attunement, you can attune yourself to it the instant you infuse the item, or you can forgo attunement so that someone else can attune to the item.",
                    "If you decide to attune to the item later, you must do so using the normal process for attunement. Your infusion remains in an item indefinitely, but when you die, the infusion vanishes after a number of days have passed equal to your Intelligence modifier (minimum of 1 day). The infusion also vanishes if you give up your knowledge of the infusion for another one.",
                    "You can infuse more than one non-magical object at the end of a long rest; the maximum number of objects appears in the Infused Items column of the Artificer table. You must touch each of the objects, and each of your infusions can be in only one object at a time. Moreover, no object can bear more than one of your infusions at a time. If you try to exceed your maximum number of infusions, the oldest infusion immediately ends, and then the new infusion applies."
            );
        }
    }
}
