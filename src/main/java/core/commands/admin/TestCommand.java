package core.commands.admin;

import echoedcore.core.commands.Command;
import echoedcore.core.commands.Modules;
import utilities.dnd.DatabaseManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Collections;
import java.util.List;

public class TestCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        for (String feature : DatabaseManager.getSupportedFeaturesBySubclass("artificer", "alchemist")) {
            mre.getChannel().sendMessage(feature).queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("test");
    }

    @Override
    public String getModule() {
        return Modules.ADMIN;
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
