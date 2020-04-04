package core.listeners;

import core.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class TagListener extends ListenerAdapter {

    User botMention = Main.api.getSelfUser();
    String args, reply;

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //super.onMessageReceived(event);

        // Check if message or author is null
        if (!event.getMessage().equals(null) && !event.getAuthor().equals(null)) {
            // Check if message mentions the bot
            if (event.getMessage().getMentionedUsers().contains(botMention)) {

                args = event.getMessage().getContentRaw().replace(botMention.getAsMention(), "");

                // Check if they are asking for prefix
                if (args.contains("prefix")) { // TODO change prefix to config used
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", the prefix is " + "/").queue();

                    return;
                }
                else {
                    // ChatterBot reply
                    try {
                        reply = Main.bot1session.think(args);

                        // Check the reply is not null before attempting to send.
                        if (reply.equals(null)) {
                            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", My conversation services seem to be malfunctioning.").queue();
                        }
                        else {
                            event.getChannel().sendMessage(reply).queue();
                        }
                    }
                    catch (Exception e) {
                        // TODO when Logger is implemented
                    }
                }
            }
        }
    }
}
