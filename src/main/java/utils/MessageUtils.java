package utils;

import core.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class MessageUtils {
    public static void addDefaults(EmbedBuilder embed) {
        // Add defaults.
        embed.setFooter("EchoedDungeons by EchoedAJ#1840", null);
        embed.setAuthor("Try `" + Main.config.getPrefix() + "help [command]` for more.");
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
}
