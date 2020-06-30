package core.commands.math;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;

import java.util.Arrays;
import java.util.List;

/**
 *  SetModeCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class SetModeCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().debug("SETMODE (called by " + mre.getAuthor().getAsTag() + ")", Constants.stageCommand);

        switch (args[1].toLowerCase()) {
            case "degrees":
            case "degree":
            case "deg":
            case "d":
                Main.getTrig().setDegrees();
                mre.getChannel().sendMessage("Set math mode to degrees.").queue();
                break;
            case "radians":
            case "radian":
            case "rad":
            case "r":
                Main.getTrig().setRadians();
                Main.getTrig().setDegrees();
                mre.getChannel().sendMessage("Set math mode to radians.").queue();
                break;
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mode", "setmode", "sm");
    }

    @Override
    public String getModule() {
        return Constants.MATH;
    }

    @Override
    public String getDescription() {
        return "Change modes between radians and degrees.";
    }

    @Override
    public String getName() {
        return "Set Degrees Command";
    }

    @Override
    public List<String> getUsage() {
        return Arrays.asList(
                "`" + Main.getConfig().getPrefix() + "setdeg [true|false]`",
                "If true, mode is set to degrees.",
                "If false, mode is set to radians."
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
