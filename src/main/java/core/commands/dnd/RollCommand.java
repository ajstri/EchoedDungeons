package core.commands.dnd;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.DiceRoll;
import utils.Logger;
import utils.exceptions.InvalidNotationException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("ROLL");
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
        input = input.replace(Main.config.getPrefix(), "").replace("\\", "");

        // Erase command name and space following it
        // The space following it was tricking the rolls into thinking there was
        // an expression ""
        for (String alias : getAliases()) {
            input = input.replace(alias + " ", "");
        }

        // Split into dice expressions
        String[] diceExpressions = input.split(" ");

        for (String diceE : diceExpressions) {
            int result = 0;
            results.clear();

            try {
                // add A or S if adding or subtracting.
                // this is given if the user starts the message
                // with an "s"
                // If it doesn't, add an "a" for our pattern so
                // it can still read it.
                if (!diceE.toLowerCase().contains("s")) {
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
                    result += roller.getModifier();

                    // Add to array for this expression
                    results.add(result);

                }

                // Comment we rolled the expression
                rolled.append(diceE).append(" ");

                // Replace "a" and "s" with user-friendly syntax
                if (rolled.toString().toLowerCase().startsWith("a")) {
                    rolled.replace(0, 1, "");
                }
                if (rolled.toString().toLowerCase().startsWith("s")) {
                    rolled.replace(0, 1, "-");
                }

                Collections.sort(results);

                // DEBUG: Outputs sorted rolls.
                if (Main.config.getDebug()) {
                    System.out.println("---");
                    System.out.println("Sorted: ");
                    for (int result1 : results) {
                        System.out.print(result1 + " ");
                    }
                    System.out.println("\n---");
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
                if (Main.config.getDebug()) {
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

                // If negative (AKA there is an "s")
                // invert the result
                if (roller.getNegative()) {
                    result = -result;
                }

            }
            catch (InvalidNotationException ine) {
                Logger.error("Unable to parse dice expression.", ine);
                mre.getChannel().sendMessage("Failed to parse your roll!").queue();
            }
            total += result;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(mre.getAuthor().getAsMention());
        builder.append(" is rolling ").append(rolled.toString());
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

        mre.getChannel().sendMessage(builder.toString()).queue();
        mre.getMessage().delete().queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("roll", "r");
    }

    @Override
    public boolean isDND() {
        return true;
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
                Main.config.getPrefix() + "roll *<dice>*\n" +
                "__Example:__ " + Main.config.getPrefix() + "roll 3d8+5\n" +
                "Extended Syntax: \n`" + Main.config.getPrefix() +
                "roll (s)[AmountOfDice]d[SidesOfDice]([+|-][Modifier])(dl|kl|kh|dh)`\n" +
                "`(optional) [required]`\n" +
                "`s` - include if you want the roll negative\n" +
                "`([+|-][Modifier])` - include full part if you need modifiers. Example: +5\n" +
                "`(dl|kl|kh|dh)` - DropLowest, KeepLowest, KeepHighest, DropHighest"
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }

}
