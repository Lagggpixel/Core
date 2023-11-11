package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.apache.logging.log4j.core.LogEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.TimeZone;

public class ConsoleHandler {
  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

  public static void onLogEvent(LogEvent logEvent) {
    MessageCreateBuilder builder = new MessageCreateBuilder();
    builder.setContent(formatLoggingMessage(logEvent));
    builder.setEmbeds(Collections.emptyList());
    DiscordManager.getInstance().CONSOLE_CHANNEL.sendMessage(builder.build()).queue();
  }

  private static String stripAnsiColors(String input) {
    String ansiColorPattern = "\u001B\\[[;\\d]*m";
    return input.replaceAll(ansiColorPattern, "");
  }

  private static String formatLoggingMessage(LogEvent event) {
    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimeMillis()), TimeZone.getDefault().toZoneId());
    return "[" + dtf.format(dateTime) + " " + event.getLevel() + "]: " + stripAnsiColors(event.getMessage().getFormattedMessage());
  }
}
