package core.commands;

import core.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RollCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("ROLL");
        // *rolls into oblivion*
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
                Main.config.getPrefix() + "roll *<dice>*\n"
                        + "__Example:__ " + Main.config.getPrefix() + "roll 3d8+5");
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
