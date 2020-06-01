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
package core.commands.music;

import core.Main;
import core.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Constants;
import utils.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  PlayCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class PlayCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Logger.info("PLAY (called by " + mre.getAuthor().getAsTag() + ")");

        Member author = mre.getMember();
        if (author == null) {
            // Somehow the author is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong.").queue();
        }
        // Check if author is in a voice channel
        else if (Main.utils.isInVoiceChannel(author)) {
            if (args.length == 2) {
                // Add song to queue
                Logger.info("Loading a track: " + args[1]);
                Main.utils.loadAndPlay(mre.getTextChannel(), args[1], mre.getMember(), Main.utils, false);
            }
            else if (args.length == 1) {
                if (Main.utils.isPaused(mre.getTextChannel())) {
                    Main.utils.continuePlaying(mre.getGuild());
                }
                else mre.getChannel().sendMessage("Please provide something to play!").queue();
            }
            // possibly overriding queue
            else if (args.length == 3) {
                if (args[2].toLowerCase().contains("first")) {
                    // Play song first
                    Main.utils.loadAndPlay(mre.getTextChannel(), args[1], mre.getMember(), Main.utils, true);
                }
            }
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("play", "pl");
    }

    @Override
    public String getModule() {
        return Constants.MUSIC;
    }

    @Override
    public String getDescription() {
        return "Plays audio from a YouTube URL";
    }

    @Override
    public String getName() {
        return "Play Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                "`" + Main.config.getPrefix() + getAliases().get(0) + "[OPTIONAL: URL] [OPTIONAL: first]\n"
                + "Write first if you want to override the queue\n"
                + "Write your URL if you are adding, ignore this if you are just restarting the player"
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
