package core.commands;

import core.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RollCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        // *rolls into oblivion*
    }

    @Override
    protected List<String> getAliases() {
        return Arrays.asList("roll", "r");
    }

    @Override
    protected String getDescription() {
        return "Roll any amount of dice plus a modifier!";
    }

    @Override
    protected String getName() {
        return "Roll Command";
    }

    @Override
    protected List<String> getUsage() {
        return Collections.singletonList(
                Main.config.getPrefix() + "roll *<dice>*\n"
                        + "__Example:__" + Main.config.getPrefix() + "roll 3d8+5");
    }

    @Override
    protected boolean getDefaultPermission() {
        return false;
    }
}
