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
package core.commands.dnd.utility;

import core.Main;
import echoedcore.core.commands.Command;
import echoedcore.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.EchoedDungeonsConstants;
import utilities.dnd.DiceRoller;

import java.util.*;

/**
 *  RollCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class RollCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getBotLogging().info("ROLL (called by " + mre.getAuthor().getAsTag() + ")");
        // *rolls into oblivion*

        DiceRoller roller;
        if (args[1].equalsIgnoreCase("stats")) {
            roller = new DiceRoller("4d6dl 4d6dl 4d6dl 4d6dl 4d6dl 4d6dl".split(" "));
        }
        else {
            roller = new DiceRoller(args);
        }

        // Skip if could not parse anything. There will be nothing
        // in rolled in this case
        if (roller.getTotals().size() != 0) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("@" + mre.getAuthor().getAsTag() + "'s roll");
            embed.setThumbnail(mre.getAuthor().getAvatarUrl());

            embed.setFooter("EchoedDungeons by EchoedAJ#1840", null);
            MessageUtilities.setTimestamp(embed);

            // Add nats if they rolled any
            if (roller.getNatural20()) {
                embed.addField("They rolled a natural 20!", "", false);
            }
            if (roller.getNatural1()) {
                embed.addField("They rolled a nat 1...", "", false);
            }

            // Add rolled dice

            embed.addField("Rolled: " + roller.getRolled().toString().replaceFirst("," , " "), "Total: " + roller.getFinalTotal(), false);

            if (args[1].equals("stats")) {
                // Output each on their own
                for (int total : roller.getTotals()) {
                    embed.addField("4d6dl", total + "", true);
                }
            }

            mre.getChannel().sendMessage(embed.build()).queue();
            mre.getMessage().delete().queue();
        }
        else {
            mre.getChannel().sendMessage("Could not parse one or more of your dice.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("roll", "r");
    }

    @Override
    public String getModule() {
        return EchoedDungeonsConstants.DND;
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
