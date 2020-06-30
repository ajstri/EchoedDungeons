package core.commands.general;

import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;

import java.util.Collections;
import java.util.List;

public class PleaseHelpCommand extends Command {
    @java.lang.Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        mre.getChannel().sendMessage("you just gotta scream like an imbecile and shit on your desk").queue();
    }

    @java.lang.Override
    public List<String> getAliases() {
        return Collections.singletonList("please");
    }

    @java.lang.Override
    public String getModule() {
        return Constants.GENERIC;
    }

    @java.lang.Override
    public String getDescription() {
        return "help";
    }

    @java.lang.Override
    public String getName() {
        return "help";
    }

    @java.lang.Override
    public List<String> getUsage() {
        return Collections.singletonList("help");
    }

    @java.lang.Override
    public boolean getDefaultPermission() {
        return true;
    }
}
