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
package utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import utils.Constants;

/**
 * MusicHandler class of the EchoedDungeons project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class MusicHandler implements AudioLoadResultHandler {

    private final TextChannel channel;
    private final String trackUrl;
    private final Member author;
    private final MusicUtils utils;
    private final boolean first;

    /**
     * MusicHandler constructor
     * @param channel Channel for the MusicHandler
     * @param trackUrl URL to handle
     * @param author Author of the initiation
     * @param utils MusicUtils
     * @param first if the Track will be first
     */
    public MusicHandler(TextChannel channel, String trackUrl, Member author, MusicUtils utils, boolean first) {
        this.channel = channel;
        this.trackUrl = trackUrl;
        this.author = author;
        this.utils = utils;
        this.first = first;
    }

    /**
     * On Track Load
     * @param audioTrack Loaded track
     */
    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        if (utils.isInVoiceChannel(author)) {
            channel.sendMessage("Adding to queue " + audioTrack.getInfo().title).queue();


            int connect = utils.play(channel.getGuild(), audioTrack, author, first);

            if (connect == Constants.VOICE_CONNECT_NO_PERMS) {
                channel.sendMessage("I don't have permission to join your voice channel!").queue();
            }
            else if (connect == Constants.VOICE_CONNECT_FAIL_OTHER) {
                channel.sendMessage("Uh oh! Something went wrong...").queue();
            }
        }
    }

    /**
     * On Playlist Load
     * @param playlist Loaded playlist
     */
    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

        for (AudioTrack audio : playlist.getTracks()) {
            if (utils.play(channel.getGuild(), audio, author, false) == -1) {
                channel.sendMessage("You are not in a voice channel!").queue();
            }
        }
    }

    /**
     * No Matches
     */
    @Override
    public void noMatches() {
        channel.sendMessage("Nothing found by " + trackUrl).queue();
    }

    /**
     * On Load Failed
     * @param e Exception of the fail
     */
    @Override
    public void loadFailed(FriendlyException e) {

    }
}
