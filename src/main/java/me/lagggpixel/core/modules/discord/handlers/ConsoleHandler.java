package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageData;
import org.apache.logging.log4j.core.LogEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.TimeZone;

public class ConsoleHandler {
  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
  private static Integer time;
  private static Message message;
  private static MessageData messageData;

  public static void onLogEvent(LogEvent logEvent) {
    /*
    Integer currentTime = NumberConversions.floor((double) LocalDateTime.now().getMinute() / 5) * 5;
    if (message == null || !currentTime.equals(time)) {
      time = NumberConversions.floor((double) LocalDateTime.now().getMinute() / 5) * 5;
      MessageCreateBuilder builder = new MessageCreateBuilder();
      builder.setContent(formatLoggingMessage(logEvent));
      builder.setEmbeds(Collections.emptyList());
      messageData = builder.build();
      DiscordManager.getInstance().CONSOLE_CHANNEL.sendMessage(builder.build()).queue((message) -> ConsoleHandler.message = message);
      return;
    }
    
    MessageEditBuilder builder = new MessageEditBuilder();
    builder.setContent(messageData.getContent() + "\n" + formatLoggingMessage(logEvent));
    messageData = builder.build();
    message.editMessage(builder.build()).queue((message) -> {
      ConsoleHandler.message = message;
    });
     */
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
