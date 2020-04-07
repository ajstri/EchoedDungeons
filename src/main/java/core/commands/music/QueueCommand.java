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
import utils.Logger;

import java.util.Arrays;
import java.util.List;

/**
 *  QueueCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class QueueCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        Member author = mre.getMember();
        if (author != null) {
            // Check if in a voice channel
            if (Main.utils.isInVoiceChannel(author)) {
                if (args.length == 1) {
                    // No page number, display first
                    Logger.info("Displaying queue");
                    Main.utils.displayQueue(mre.getTextChannel(), 1);
                }
                else {
                    int page = 1;
                    try {
                        // Set page to argument
                        page = Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException nfe) {
                        mre.getChannel().sendMessage("If you want to visit a page in the queue, please use an integer.").queue();
                    }
                    // Actually display queue
                    Main.utils.displayQueue(mre.getTextChannel(), page);
                }
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // Author's member is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("queue", "q");
    }

    @Override
    public boolean isDND() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getUsage() {
        return null;
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }
}
