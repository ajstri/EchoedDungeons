/*
 *  Copyright 2020 EchoedAJ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package core.commands.admin;

import core.Main;
import echoedcore.core.EchoedCore;
import echoedcore.core.commands.Modules;
import echoedcore.utilities.Constants;
import echoedcore.core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.EchoedDungeonsConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  ShutdownCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class ShutdownCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getBotLogging().info("SHUTDOWN (called by " + mre.getAuthor().getAsTag() + ")");

        // Determine if the message came from the Owner, EchoedAJ#1840
        String id = mre.getAuthor().getId();
        if (id.contains(EchoedDungeonsConstants.OWNER_ID)) {
            // Shutdown
            mre.getChannel().sendMessage("Okay, AJ.").complete();
            EchoedCore.shutdown(Constants.STATUS_FRIENDLY);
        }
        else {
            // Be offended, it's not AJ
            mre.getChannel().sendMessage("How dare you?").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("shutdown", "sd", "rest");
    }

    @Override
    public String getModule() {
        return Modules.ADMIN;
    }

    @Override
    public String getDescription() {
        return "Shuts down the Bot.";
    }

    @Override
    public String getName() {
        return "Shutdown Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + "sd`");
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
