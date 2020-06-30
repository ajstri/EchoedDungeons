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
package core.commands.dnd;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.DiceRoll;
import utilities.MessageUtilities;
import utilities.exceptions.InvalidNotationException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("ROLL (called by " + mre.getAuthor().getAsTag() + ")");
        // *rolls into oblivion*

        String nat20Expression = "No";
        String nat1Expression = "No";
        StringBuilder rolled = new StringBuilder();
        int total = 0;
        List<Integer> results = new ArrayList<>();

        // Add the args together in order to get it ready to parse
        // Keep the pieces separate by " "
        StringBuilder expression = new StringBuilder();
        for (String arg : args) {
            expression.append(arg).append(" ");
        }

        // Replace possible escapes (when a user avoids markdown formatting)
        String input = expression.toString();
        input = input.replace(Main.getConfig().getPrefix(), "").replace("\\", "");

        // Erase command name and space following it
        // The space following it was tricking the rolls into thinking there was
        // an expression ""
        for (String alias : getAliases()) {
            input = input.replace(alias + " ", "");
        }

        Main.getLog().info("Rolling: " + input);

        // Split into dice expressions
        String[] diceExpressions = input.split(" ");

        for (String diceE : diceExpressions) {
            int result = 0;
            results.clear();

            try {
                // add a 1 to the start if they did not
                // themselves. aka: d20 into 1d20
                if (diceE.toLowerCase().startsWith("d")) {
                    diceE = "1" + diceE;
                }

                // check if it doesn't have a 1
                // in the case they add an "s"
                if (diceE.toLowerCase().startsWith("sd")) {
                    diceE = diceE.replace("sd", "s1d");
                }

                // add A or S if adding or subtracting.
                // this is given if the user starts the message
                // with an "s"
                // If it doesn't, add an "a" for our pattern so
                // it can still read it.
                if (!diceE.toLowerCase().contains("s") && !diceE.toLowerCase().contains("a")) {
                    diceE = "a" + diceE;
                }

                // Initialize the roller for this expression
                DiceRoll roller = new DiceRoll(diceE);

                // For each die: 1dX runs once, etc.
                for (int i = 1; i <= roller.getNumDice(); i++) {
                    // Generate random result
                    result = ThreadLocalRandom.current().nextInt(1, roller.getNumSides() + 1);

                    // Note the NATs
                    if (result == 20) {
                        nat20Expression = diceE;
                    }
                    if (result == 1) {
                        nat1Expression = diceE;
                    }

                    // Add modifier
                    // result += roller.getModifier();

                    // Add to array for this expression
                    results.add(result);

                }

                // Replace "a" and "s" with user-friendly syntax
                if (diceE.toLowerCase().startsWith("a")) {
                    diceE = diceE.replace("a", "");
                }
                if (diceE.toLowerCase().startsWith("s")) {
                    diceE = diceE.replace("s", "-");
                }

                // Comment we rolled the expression
                rolled.append(", ").append(diceE);

                Collections.sort(results);

                // DEBUG: Outputs sorted rolls.
                if (Main.getConfig().getDebug()) {
                    System.out.println("---");
                    System.out.println("Dice Expression: " + diceE);
                    System.out.println("Sorted: ");
                    for (int result1 : results) {
                        System.out.print(result1 + " ");
                    }
                    System.out.println();
                }

                // Check for drops.
                // kl | kh | dl | dh
                if (roller.dropWasSpecified()) {
                    if (roller.getDropLow() > 0) { // Drop lowest
                        for (int i = 0; i < roller.getDropLow(); i++) {
                            // Replace with 0. Will still add, but no effect
                            results.set(i, 0);
                        }
                    }
                    if (roller.getDropHigh() > 0) { // Drop highest
                        for (int i = results.size() - 1; i > results.size() - 1 - roller.getDropHigh(); i--) {
                            // Replace with 0. Will still add, but no effect
                            results.set(i, 0);
                        }
                    }
                }

                // DEBUG: Outputs sorted rolls after drops.
                if (Main.getConfig().getDebug()) {
                    System.out.println("After drops: ");
                    for (int rolls : results) {
                        System.out.print(rolls + " ");
                    }
                    System.out.println();
                }

                // Add rolls together
                result = 0;
                for (int rolls : results) {
                    result += rolls;
                }

                if (Main.getConfig().getDebug()) {
                    System.out.println("Total: " + result);
                }

                // Add modifier
                result += roller.getModifier();

                // If negative (AKA there is an "s")
                // invert the result
                if (roller.getNegative()) {
                    result = -result;
                }

                if (Main.getConfig().getDebug()) {
                    System.out.println("Total (after modifiers): " + result);
                }

            }
            catch (InvalidNotationException ine) {
                Main.getLog().error("Unable to parse dice expression.", ine);
                mre.getChannel().sendMessage("Failed to parse your roll!").queue();
            }
            total += result;
        }

        if (Main.getConfig().getDebug()) {
            System.out.println("---");
            System.out.println("Final Result: " + total);
        }

        // Build & send message.
        /*StringBuilder builder = new StringBuilder();

        builder.append("```");
        builder.append(mre.getAuthor().getAsTag());
        builder.append(" is rolling ").append(rolled.toString()).replace(builder.length() - 2, builder.length(), "");
        builder.append("\n");
        if (!nat20Expression.toLowerCase().contains("no")) {
            builder.append("They rolled a nat 20 on ").append(nat20Expression).append("!");
            builder.append("\n");
        }
        if (!nat1Expression.toLowerCase().contains("no")) {
            builder.append("They rolled a nat 1 on ").append(nat1Expression);
            builder.append("\n");
        }
        builder.append("They rolled a total of ").append(total);
        builder.append("```");

        mre.getChannel().sendMessage(builder.toString()).queue();
        mre.getMessage().delete().queue();*/

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("@" + mre.getAuthor().getAsTag() + "'s roll");
        embed.setThumbnail(mre.getAuthor().getAvatarUrl());

        embed.setFooter("EchoedDungeons by EchoedAJ#1840", null);
        MessageUtilities.setTimestamp(embed);

        if (!nat20Expression.toLowerCase().contains("no")) {
            embed.addField("They rolled a nat 20 on " + nat20Expression.replace("a", "").replace("s", "") + "!", "", false);
        }
        if (!nat1Expression.toLowerCase().contains("no")) {
            embed.addField("They rolled a nat 1 on " + nat1Expression.replace("a", "").replace("s", "") + "...", "", false);
        }

        embed.addField(rolled.toString().replaceFirst(", ", ""), "Total: " + total, false);

        mre.getChannel().sendMessage(embed.build()).queue();
        mre.getMessage().delete().queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("roll", "r");
    }

    @Override
    public String getModule() {
        return Constants.DND;
    }

    @Override
    public String getDescription() {
        return "Roll any amount of dice plus a modifier!";
    }

    @Override
    public String getName() {
        return "Roll Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                Main.getConfig().getPrefix() + "roll *<dice>*\n" +
                "__Example:__ " + Main.getConfig().getPrefix() + "roll 3d8+5\n" +
                "Extended Syntax: \n`" + Main.getConfig().getPrefix() +
                "roll (s)[AmountOfDice]d[SidesOfDice]([+|-][Modifier])(dl|kl|kh|dh)`\n" +
                "`(optional) [required]`\n" +
                "`s` - include if you want the roll negative\n" +
                "`([+|-][Modifier])` - include full part if you need modifiers. Example: +5\n" +
                "`(dl|kl|kh|dh)` - DropLowest, KeepLowest, KeepHighest, DropHighest"
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

}
