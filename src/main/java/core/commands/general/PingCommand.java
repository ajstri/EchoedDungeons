/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package core.commands.general;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PingCommand extends Command {
    @Override
    public void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("PING");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ping", "pong");
    }

    @Override
    public boolean isDND() {
        return false;
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
        return Collections.singletonList(
                "\n`" + Main.config.getPrefix() + "ping` - Returns ping of Bot\n\n"
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
