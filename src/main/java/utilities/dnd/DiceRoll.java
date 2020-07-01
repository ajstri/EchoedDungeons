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
package utilities.dnd;

import utilities.exceptions.InvalidNotationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceRoll { // TODO javadoc

    private int numDice;
    private int numSides;
    private int modifier;
    private boolean dropWasSpecified;
    private int dropLow;
    private int dropHigh;
    private boolean negative;

    private static final Pattern ROLL20_REGEX = Pattern.compile(
            "^\\s*" +
                    "(?:(?<negative>[as]?))?" +
                    "(?<numDice>\\d+)d(?<numSides>\\d+)\\s*" +
                    "(?:(?<plusMinus>[+-])\\s*(?<plusMinusValue>\\d+))?" +
                    "(?:(?<dropKeepMode>[dk][hl]?)(?<dropKeepNum>\\d*))?\\s*" +
                    "\\s*$"
    );

    public DiceRoll(String source) throws InvalidNotationException {
        Matcher matcher = ROLL20_REGEX.matcher(source);
        if (!matcher.matches()) throw new InvalidNotationException("No match!");
        parseCore(matcher);
        parseDropKeep(matcher);
        parseModifier(matcher);
        parseNegative(matcher);
    }

    private void parseCore(Matcher matcher) {
        numDice = Integer.parseInt(matcher.group("numDice"));
        numSides = Integer.parseInt(matcher.group("numSides"));
    }

    private void parseNegative(Matcher matcher) {
        if (matcher.group("negative") == null) {
            negative = false;
        }

        negative = matcher.group("negative").toLowerCase().contains("s");
    }

    private void parseDropKeep(Matcher matcher) throws InvalidNotationException {
        if (matcher.group("dropKeepMode") == null) {
            return;
        }
        dropWasSpecified = true;

        String dropKeepNumberStr = matcher.group("dropKeepNum");
        int dropKeepNumber = dropKeepNumberStr.isEmpty() ?
                1 : Integer.parseInt(dropKeepNumberStr);
        switch (matcher.group("dropKeepMode")) {
            case "d": //Drop lowest
            case "dl":
                dropLow = dropKeepNumber;
                break;
            case "k": //Keep highest
            case "kh":
                dropLow = numDice - dropKeepNumber;
                break;
            case "dh": //Drop highest
                dropHigh = dropKeepNumber;
                break;
            case "kl": //Keep lowest
                dropHigh = numDice - dropKeepNumber;
                break;
        }
        if (dropLow > numDice || dropHigh > numDice) {
            throw new InvalidNotationException("Number of drops can't be greater than the number of dice");
        }
        dropWasSpecified = true;
    }

    private void parseModifier(Matcher matcher) {
        modifier = (matcher.group("plusMinus") == null) ?
                0 :
                Integer.parseInt(
                        matcher.group("plusMinus") + matcher.group("plusMinusValue")
                );
    }

    public int getNumDice() {
        return numDice;
    }

    public int getNumSides() {
        return numSides;
    }

    public int getModifier() {
        return modifier;
    }

    public boolean dropWasSpecified() {
        return dropWasSpecified;
    }

    public int getDropLow() {
        return dropLow;
    }

    public int getDropHigh() {
        return dropHigh;
    }

    public boolean getNegative() {
        return negative;
    }

    public String toString() {
        return String.format("%dd%d%s%s",
                numDice,
                numSides,
                ((dropLow > 0)       ? String.format(" dl%d", dropLow) :
                        (dropHigh > 0)      ? String.format(" dh%d", dropHigh) :
                                ""),
                ((modifier == 0) ? "" : String.format(" %+d", modifier))
        );
    }

}