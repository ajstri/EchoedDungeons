package core.commands;

import core.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class PingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent mre, String[] args) {
        mre.getChannel().sendMessage("Really, AJ? You forgot to actually fill out the command body?").queue();
        mre.getChannel().sendMessage("That's why there was nothing coming. You should have looked at the fucking CONSOLE").queue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ping", "pong");
    }

    @Override
    public String getDescription() {
        return "Pong!";
    }

    @Override
    public String getName() {
        return "Ping Command";
    }

    @Override
    public List<String> getUsage() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList("\n`" + Main.config.getPrefix() + "ping` - Returns ping of Bot\n\n");
    }

    @Override
    protected boolean getDefaultPermission() {
        return true;
    }
}
