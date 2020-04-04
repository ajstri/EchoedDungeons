package core.commands;

import core.Main;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.Constants;
import utils.Logger;

import java.util.List;

public abstract class Command extends ListenerAdapter {

    protected abstract void onCommand(MessageReceivedEvent mre, String[] args);
    protected abstract List<String> getAliases();
    protected abstract String getDescription();
    protected abstract String getName();
    protected abstract List<String> getUsage();
    protected abstract boolean getDefaultPermission();

    /**
     * Called when Message is received visible to the Bot.
     * Determines if there was a command called, and if so,
     * executes the command.
     * @param mre Event triggered
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent mre) {
        if (mre.getAuthor().isBot() && !respondToBots()) {
            return;
        }
        if(commandArgs(mre.getMessage())[0].contains(Main.config.getPrefix()) && containsCommand(mre.getMessage())) {
            Logger.debug("Calling a command.", Constants.stageCommand);
            onCommand(mre, commandArgs(mre.getMessage()));
        }

    }

    /**
     * Determines if the message contained a command.
     * @param message message to scan
     * @return true if message contained command, false if not
     */
    private boolean containsCommand(Message message) {
        return getAliases().contains(commandArgs(message)[0].replace(Main.config.getPrefix(), ""));
    }

    /**
     * Breaks up the message into String[].
     * @param message message to split
     * @return String[] of the split message
     */
    private String[] commandArgs(Message message) {
        return commandArgs(message.getContentDisplay());
    }

    /**
     * Breaks up the message into String[].
     * @param string message to split
     * @return String[] of the split message
     */
    private String[] commandArgs(String string) {
        return string.split(" ");
    }

    /**
     * Sends a message to the given channel the Event was
     * triggered in.
     * @param mre Event triggered
     * @param message Message to send
     * @return message to send
     */
    private Message sendMessage(MessageReceivedEvent mre, Message message) {
        if(mre.isFromType(ChannelType.PRIVATE)) {
            return mre.getPrivateChannel().sendMessage(message).complete();
        }
        else {
            return mre.getTextChannel().sendMessage(message).complete();
        }
    }

    /**
     * Sends a message to the given channel the Event was
     * triggered in.
     * @param mre Event triggered
     * @param message Message to send
     * @return message to send
     */
    @SuppressWarnings("UnusedReturnValue")
    Message sendMessage(MessageReceivedEvent mre, String message) {
        return sendMessage(mre, new MessageBuilder().append(message).build());
    }

    /**
     * Determines if the Bot will respond to other Bots.
     * @return true if the Bot will respond to other Bots,
     * false if not
     */
    @SuppressWarnings("SameReturnValue")
    private boolean respondToBots() {
        return false;
    }
}
