/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.modules.discord.DiscordModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.javacord.api.entity.message.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class ConsoleHandler {
  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
  private static long lastMessageMillis = -1L;
  private static String currentContent;

  public static void onLogEvent(LogEvent logEvent) {
    if (lastMessageMillis == -1L) {
      lastMessageMillis = System.currentTimeMillis();
    }
    
    if (System.currentTimeMillis() - lastMessageMillis < 1000L) {
      if (StringUtils.isBlank(currentContent)) {
        currentContent = formatLoggingMessage(logEvent);
        return;
      }
      currentContent = currentContent + "\n" + formatLoggingMessage(logEvent);
      return;
    }
    DiscordModule.discordHandler.CONSOLE_CHANNEL.sendMessage(currentContent)
        .thenAccept(Message::removeEmbed);
    lastMessageMillis = System.currentTimeMillis();
    currentContent = "";
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
