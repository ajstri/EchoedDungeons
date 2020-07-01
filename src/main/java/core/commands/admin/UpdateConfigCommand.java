package core.commands.admin;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  ClassCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since July 2020
 */
public class UpdateConfigCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        String userID = mre.getAuthor().getId();

        if (!userID.equals(Constants.OWNER_ID)) {
            mre.getChannel().sendMessage("This command can only be used by the owner.").queue();
            return;
        }

        if (args.length < 2) {
            mre.getChannel().sendMessage("Please define a value to view or update.").queue();
        }
        else if (args.length < 3) {
            // Supported: Prefix and Debug
            if (args[1].toLowerCase().equals("prefix")) {
                mre.getChannel().sendMessage("Prefix is currently: " + Main.getConfig().getPrefix()).queue();
            }
            else if (args[1].toLowerCase().equals("debug")) {
                mre.getChannel().sendMessage("Debug is currently: " + Main.getConfig().getDebug()).queue();
            }
        }
        else {
            if (args[1].toLowerCase().equals("prefix")) {
                Main.getConfig().setPrefix(args[2]);
                mre.getChannel().sendMessage("Prefix updated to: **" + args[2] + "**").queue();
            }
            else if (args[1].toLowerCase().equals("debug")) {
                if (args[2].toLowerCase().equals("true")) {
                    Main.getConfig().setDebug(true);
                    mre.getChannel().sendMessage("Debug updated to: **" + args[2] + "**").queue();
                }
                else if (args[2].toLowerCase().equals("false")) {
                    Main.getConfig().setDebug(false);
                    mre.getChannel().sendMessage("Debug updated to: **" + args[2] + "**").queue();
                }
                else {
                    mre.getChannel().sendMessage("Please only use **true** or **false**.").queue();
                }
            }
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("uc", "updateconfig", "update");
    }

    @Override
    public String getModule() {
        return Constants.ADMIN;
    }

    @Override
    public String getDescription() {
        return "Sets a new prefix for the Bot.";
    }

    @Override
    public String getName() {
        return "Prefix Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "setprefix [newPrefix]`");
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
