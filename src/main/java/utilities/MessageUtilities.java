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
package utilities;

import core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class MessageUtilities {
    public static void addEmbedDefaults(EmbedBuilder embed) {
        // Add defaults.
        embed.setFooter("EchoedDungeons by EchoedAJ#1840", null);
        embed.setAuthor("Try `" + Main.getConfig().getPrefix() + "help [command]` for more.");
        setTimestamp(embed);
    }

    public static void setTimestamp(EmbedBuilder embed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);

        embed.setTimestamp(temporalAccessor);
    }

    public static void sendIfNotPrivate(MessageReceivedEvent mre) {
        // Bypass sending message if it is already in a private message.
        if(!mre.isFromType(ChannelType.PRIVATE)) {
            // Send help message
            mre.getTextChannel().sendMessage(new MessageBuilder()
                    .append("Hey, ")
                    .append(mre.getAuthor())
                    .append(": Help information was sent as a private message.")
                    .build()).queue();
        }
    }

    public static EmbedBuilder embedBotInfo() {
        EmbedBuilder embed = new EmbedBuilder();

        addEmbedDefaults(embed);

        // Add information fields
        embed.setThumbnail("https://cdn.discordapp.com/attachments/693741051327807549/711698762682073108/image0.png");
        embed.addField("Version", Constants.VERSION + "." + Constants.BUILD_NUMBER, true);
        embed.addField("Current Prefix", Main.getConfig().getPrefix(), true);
        embed.addField("Author", "EchoedAJ#1840 ->", true);

        embed.addField("Special Thanks To", "Moisty McMoisterson\nBrad G.\nNoah", false);

        return embed;
    }
}