/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.Arrays;

public class ExceptionUtils {
  public static void handleException(Throwable e) {
    TextChannel channel = DiscordHandler.getInstance().LOGGING_CHANNEL;
    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.RED);
    embedBuilder.setAuthor("An exception has occurred");
    embedBuilder.addField("Stack Trace", Arrays.toString(e.getStackTrace()), false);
    embedBuilder.setFooter("Exception" + e.getClass().getCanonicalName());
    DiscordHandler.getInstance().sendEmbed(channel, embedBuilder.build());
    throw new RuntimeException(e);
  }
}
