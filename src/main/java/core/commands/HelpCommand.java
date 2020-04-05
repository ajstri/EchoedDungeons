package core.commands;

import core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import utils.EmbedUtils;
import utils.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;

public class HelpCommand extends Command {
    private static final String NO_NAME = "No name provided for this command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

    public final TreeMap<String, Command> commands;

    public HelpCommand() {
        commands = new TreeMap<>();
    }

    /**
     * Adds command to TreeMap.
     * @param command command to add
     * @return command added
     */
    public Command registerCommand(Command command) {
        commands.put(command.getAliases().get(0), command);
        return command;
    }

    @Override
    public void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("HELP");
        // Bypass sending message if it is already in a private message.
        String helpCommand = args[0];
        if (helpCommand.toLowerCase().contains("dnd")) {
            if(!mre.isFromType(ChannelType.PRIVATE)) {
                mre.getTextChannel().sendMessage(new MessageBuilder()
                        .append("Hey, ")
                        .append(mre.getAuthor())
                        .append(": Help information was sent as a private message.")
                        .build()).queue();
            }
            sendPrivateDND(mre.getAuthor().openPrivateChannel().complete(), args);
        }
        else {
            if(!mre.isFromType(ChannelType.PRIVATE)) {
                mre.getTextChannel().sendMessage(new MessageBuilder()
                        .append("Hey, ")
                        .append(mre.getAuthor())
                        .append(": Help information was sent as a private message.")
                        .build()).queue();
            }
            sendPrivateDefault(mre.getAuthor().openPrivateChannel().complete(), args);
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("help", "commands", "dndhelp", "dndcommands");
    }

    @Override
    protected boolean isDND() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Command that helps use all other commands!";
    }

    @Override
    public String getName() {
        return "Help Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                Main.config.getPrefix() + "help   **OR**  " + Main.config.getPrefix() + "help *<command>*\n"
                        + Main.config.getPrefix() + "help - returns the list of commands along with a simple description of each.\n"
                        + Main.config.getPrefix() + "help <command> - returns the name, description, aliases and usage information of a command.\n"
                        + "   - This can use the aliases of a command as input as well.\n"
                        + "__Example:__ " + Main.config.getPrefix() + "help ping");
    }

    @Override
    protected boolean getDefaultPermission() {
        return true;
    }

    /**
     * Sends a message to a private channel.
     * @param channel channel to send to
     * @param args arguments used to build message
     */
    private void sendPrivateDefault(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Commands Supported").setColor(Color.RED);

            EmbedUtils.addDefaults(embed);

            // For each command, add its values to the embed.
            for (Command c : commands.values()) {
                if (!c.isDND()) {
                    String description = c.getDescription();
                    description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

                    embed.addField(c.getAliases().get(0), description, false);
                }
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();

            EmbedUtils.addDefaults(embed);

            String command = args[1];
            // Check each command. If it is the command searched for, build an embed.
            for (Command c : commands.values()) {
                if (c.getAliases().contains(command)) {
                    // Define values.
                    if (!c.isDND()) {
                        addCommandValues(embed, c);

                        // Send embed.
                        channel.sendMessage(embed.build()).queue();
                        return;
                    }
                    else {
                        addCommandValues(embed, c);
                        channel.sendMessage(embed.build()).queue();
                        channel.sendMessage("Were you looking for more D&D commands? Try `" + Main.config.getPrefix() + "dndhelp` for more.").queue();
                    }
                }
            }
            // If it reaches this point, the command searched for does not exist.
            doesNotExist(channel, args);
        }
    }

    private void sendPrivateDND(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Commands Supported").setColor(Color.RED);

            EmbedUtils.addDefaults(embed);

            // For each command, add its values to the embed.
            for (Command c : commands.values()) {
                if (c.isDND()) {
                    String description = c.getDescription();
                    description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

                    embed.addField(c.getAliases().get(0), description, false);
                }
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();

            EmbedUtils.addDefaults(embed);

            String command = args[1];
            // Check each command. If it is the command searched for, build an embed.
            for (Command c : commands.values()) {
                if (c.getAliases().contains(command)) {
                    // Define values.
                    if (c.isDND()) {
                        addCommandValues(embed, c);

                        // Send embed.
                        channel.sendMessage(embed.build()).queue();
                        return;
                    }
                }
            }
            // If it reaches this point, the command searched for does not exist.
            doesNotExist(channel, args);
        }
    }

    private static void doesNotExist(PrivateChannel channel, String[] args) {
        // If it reaches this point, the command searched for does not exist.
        channel.sendMessage(new MessageBuilder()
                .append("The provided command '**")
                .append(args[1])
                .append("**' does not exist. Use ")
                .append(Main.config.getPrefix())
                .append("help to list all commands.")
                .build()).queue();
    }

    private void addCommandValues(EmbedBuilder embed, Command c) {
        String name = c.getName();
        String description = c.getDescription();
        List<String> usageInstructions = c.getUsage();

        // Null checks.
        if (name == null || name.isEmpty()) {
            name = NO_NAME;
        }
        if (description == null || description.isEmpty()) {
            description = NO_DESCRIPTION;
        }
        if (usageInstructions == null || usageInstructions.isEmpty()) {
            usageInstructions = Collections.singletonList(NO_USAGE);
        }

        // Add values to embed.
        embed.setTitle(name);
        embed.addField(description, "", false);
        embed.addField("Aliases", StringUtils.join(c.getAliases(), ", ") + "\n", false);
        embed.addField("Usage", usageInstructions.get(0), false);

        // Add remaining usage instructions.
        for (int i = 1; i < usageInstructions.size(); i++) {
            embed.addField("", usageInstructions.get(i), false);
        }
    }
}
