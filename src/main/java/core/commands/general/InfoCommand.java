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
package core.commands.general;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;

import java.util.Collections;
import java.util.List;

public class InfoCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("INFO (called by " + mre.getAuthor().getAsTag() + ")");

        mre.getChannel().sendMessage(MessageUtilities.embedBotInfo().build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("info");
    }

    @Override
    public String getModule() {
        return Constants.MUSIC;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "Information Command";
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
