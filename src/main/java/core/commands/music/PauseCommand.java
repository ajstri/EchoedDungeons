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
package core.commands.music;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  PauseCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class PauseCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Main.getLog().info("PAUSE (called by " + mre.getAuthor().getAsTag() + ")");

        Member author = mre.getMember();
        // Check if in a voice channel
        if (author != null) {
            if (Main.getMusicUtils().isInVoiceChannel(mre.getMember())) {
                // pause
                Main.getLog().info("Pausing track");
                Main.getMusicUtils().pause(mre.getGuild());
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // Member is null
            mre.getChannel().sendMessage("Uh oh! Something went wrong.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pause", "p");
    }

    @Override
    public String getModule() {
        return Constants.MUSIC;
    }

    @Override
    public String getDescription() {
        return "Pauses the music player";
    }

    @Override
    public String getName() {
        return "Pause Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + Main.getConfig().getPrefix() + "pause`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
