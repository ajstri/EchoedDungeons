package utilities.dnd;

import core.Main;
import utilities.exceptions.InvalidNotationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiceRoller {

    private int finalTotal;
    private final List<Integer> totals = new ArrayList<>();

    StringBuilder rolled = new StringBuilder();

    private boolean natural20;
    private boolean natural1;

    public DiceRoller (String[] userInput) {
        // Prepare user input for parsing
        // Add the args together in order to get it ready to parse
        // Keep the pieces separate by " "
        StringBuilder expression = new StringBuilder();
        for (String arg : userInput) {
            expression.append(arg).append(" ");
        }

        // Replace possible escapes (when a user avoids markdown formatting)
        String input = expression.toString();
        input = input.replace(Main.getConfig().getPrefix(), "").replace("\\", "");

        // Erase command name and space following it
        // The space following it was tricking the rolls into thinking there was
        // an expression ""
        input = input.replace("roll ", "").replace("r ", "");

        Main.getBotLogging().info("Rolling: " + input);

        // Split into dice expressions
        String[] diceExpressions = input.split(" ");

        roll(diceExpressions);
    }

    private void roll(String[] diceExpressions) {
        List<Integer> results = new ArrayList<>();

        for (String diceE : diceExpressions) {
            int result;
            results.clear();

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

            // check if it doesn't have a 1
            // in the case they add an "a"
            if (diceE.toLowerCase().startsWith("ad")) {
                diceE = diceE.replace("ad", "a1d");
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
            Dice roller = parseDie(diceE);

            if (roller != null) {
                // For each die: 1dX runs once, 2dX runs twice, etc.
                for (int i = 1; i <= roller.getNumDice(); i++) {
                    // Generate random result
                    // Between 1 (inclusive) and number of sides + 1 (normally exclusive
                    // so add one to make it inclusive)
                    result = ThreadLocalRandom.current().nextInt(1, roller.getNumSides() + 1);

                    // Note the NATs, but only if its a d20
                    if (result == 20 && roller.getNumSides() == 20) {
                        natural20 = true;
                    }
                    if (result == 1 && roller.getNumSides() == 20) {
                        natural1 = true;
                    }

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

                // Add rolls together for this expression
                result = 0;
                for (int rolls : results) {
                    result += rolls;
                }

                // DEBUG: Output that total
                if (Main.getConfig().getDebug()) {
                    System.out.println("Total: " + result);
                }

                // Add modifier to the total
                result += roller.getModifier();

                // If negative (AKA there is an "s")
                // invert the result
                if (roller.getNegative()) {
                    result = -result;
                }

                // DEBUG: Output after modifiers
                if (Main.getConfig().getDebug()) {
                    System.out.println("Total (after modifiers): " + result);
                }

                // Add this expression to the total of all expressions
                totals.add(result);
                finalTotal += result;
            }
        }

        // DEBUG: Output final total of all expressions
        if (Main.getConfig().getDebug()) {
            System.out.println("---");
            System.out.println("Final Result: " + finalTotal);
        }
    }

    private Dice parseDie(String singleDie) {
        Dice die = null;
        try {
            die = new Dice(singleDie);
        }
        catch (InvalidNotationException e) {
            Main.getBotLogging().error("Invalid dice notation.", e);
        }
        return die;
    }

    public boolean getNatural20() {
        return natural20;
    }

    public boolean getNatural1() {
        return natural1;
    }

    public StringBuilder getRolled() {
        return rolled;
    }

    public int getFinalTotal() {
        return finalTotal;
    }

    public List<Integer> getTotals() {
        return totals;
    }
}
