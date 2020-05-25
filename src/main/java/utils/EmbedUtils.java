package utils;

import core.Main;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class EmbedUtils {
    public static void addDefaults(EmbedBuilder embed) {
        // Add defaults.
        embed.setFooter("EchoedDungeons by EchoedAJ#1840", null);
        embed.setAuthor("Hello! Thank you for choosing me. If you're looking for my D&D commands, please type `" + Main.config.getPrefix() + "dndhelp`.");
        embed.addField("", "Try `" + Main.config.getPrefix() + "help [command]` for more.", false);
        setTimestamp(embed);
    }

    public static void setTimestamp(EmbedBuilder embed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);

        embed.setTimestamp(temporalAccessor);
    }
}
