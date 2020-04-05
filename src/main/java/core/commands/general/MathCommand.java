package core.commands.general;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Constants;
import utils.EmbedUtils;
import utils.Logger;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class MathCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("MATH");

        if (args[1].contains("supported") || args[1].contains("functions")) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Functions Supported").setColor(Color.RED);
            EmbedUtils.addDefaults(embed);

            embed.addField("Addition", "Example: 2 + 2", false);
            embed.addField("Subtraction", "Example: 2 - 2", false);
            embed.addField("Multiplication", "Example: 2 * 2", false);
            embed.addField("Division", "Example: 2 / 2", false);

            embed.addField("Sine", "Example: sin(90), sinh(90), or asin(90)", false);
            embed.addField("Tangent", "Example: tan(90), tanh(90), or atan(90)", false);
            embed.addField("Cosine", "Example: cos(90), cosh(90), or acos(90)", false);

            embed.addField("Logarithmic", "Example: ln(10) or log(10)", false);

            embed.addField("Roots", "Example: cbrt(8) or sqrt(4)", false);

            if(!mre.isFromType(ChannelType.PRIVATE)) {
                mre.getTextChannel().sendMessage(new MessageBuilder()
                        .append("Hey, ")
                        .append(mre.getAuthor())
                        .append(": Help information was sent as a private message.")
                        .build()).queue();
            }
            mre.getAuthor().openPrivateChannel().complete().sendMessage(embed.build()).queue();
        }
        else {

            Logger.info("MATH");
            StringBuilder expression = new StringBuilder();
            for (String arg : args) {
                expression.append(arg);
            }
            String input = expression.toString();
            input = input.replace(Main.config.getPrefix(), "").replace("\\", "");

            for (String alias : getAliases()) {
                input = input.replace(alias, "");
            }

            double result = eval(input, mre.getChannel());
            Logger.debug("Result is " + result, Constants.stageCommand);
            mre.getChannel().sendMessage(Double.toString(result)).queue();
        }
    }

    @Override
    protected List<String> getAliases() {
        return Arrays.asList("math", "eval", "m");
    }

    @Override
    protected boolean isDND() {
        return false;
    }

    @Override
    protected String getDescription() {
        return "Evaluates a mathematical expression.";
    }

    @Override
    protected String getName() {
        return "Math Command";
    }

    @Override
    protected List<String> getUsage() {
        return null;
    }

    @Override
    protected boolean getDefaultPermission() {
        return false;
    }

    public static double eval(String str, MessageChannel c) {
        Logger.debug("Parsing: " + str, Constants.stageCommand);
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    Logger.error("Unexpected: " + (char)ch, new RuntimeException("Unexpected: " + (char)ch));
                    c.sendMessage("Unexpected: " + (char)ch).queue();
                    return 0;
                }
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        x += parseTerm(); // addition
                    }
                    else if (eat('-')) {
                        x -= parseTerm(); // subtraction
                    }
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) {
                        x *= parseFactor(); // multiplication
                    }
                    else if (eat('/')) {
                        x /= parseFactor(); // division
                    }
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) {
                        Logger.error("Missing closing parenthesis", new RuntimeException("Missing closing parenthesis"));
                        return 0;
                    }
                }
                else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "cbrt":
                            x = Math.cbrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        case "sinh":
                            x = Math.sinh(Math.toRadians(x));
                            break;
                        case "cosh":
                            x = Math.cosh(Math.toRadians(x));
                            break;
                        case "tanh":
                            x = Math.tanh(Math.toRadians(x));
                            break;
                        case "asin":
                            x = Math.asin(Math.toRadians(x));
                            break;
                        case "acos":
                            x = Math.acos(Math.toRadians(x));
                            break;
                        case "atan":
                            x = Math.atan(Math.toRadians(x));
                            break;
                        case "abs":
                            x = Math.abs(x);
                            break;
                        case "ln":
                            x = Math.log(x);
                            break;
                        case "log":
                            x = Math.log10(x);
                            break;
                        default:
                            Logger.error("Unknown function: " + func, new RuntimeException("Unknown function: " + func));
                            c.sendMessage("Unknown function: " + func).queue();
                            return 0;
                    }
                }
                else {
                    Logger.error("Unexpected: " + (char)ch, new RuntimeException("Unexpected: " + (char)ch));
                    c.sendMessage("Unexpected: " + (char)ch).queue();
                    return 0;
                }

                if (eat('^')) {
                    x = Math.pow(x, parseFactor()); // exponentiation
                }

                return x;
            }
        }.parse();
    }
}
