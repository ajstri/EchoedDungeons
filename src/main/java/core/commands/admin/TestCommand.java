package core.commands.admin;

import core.commands.Command;
import utilities.dnd.DatabaseManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import java.util.Collections;
import java.util.List;

public class TestCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        mre.getChannel().sendMessage(DatabaseManager.listAllLanguages().build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("test");
    }

    @Override
    public String getModule() {
        return Constants.ADMIN;
    }

    @Override
    public String getDescription() {
        return "test";
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("test");
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
