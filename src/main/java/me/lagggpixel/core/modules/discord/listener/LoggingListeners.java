/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.listener;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import me.lagggpixel.core.modules.economy.events.BalanceTopUpdateEvent;
import me.lagggpixel.core.modules.skills.events.SkillLevelUpEvent;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class LoggingListeners implements Listener {

  private final ServerTextChannel loggingChannel;

  public LoggingListeners() {
    this.loggingChannel = DiscordHandler.getInstance().LOGGING_CHANNEL;

    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void PlayerJoinEvent(@NotNull PlayerLoginEvent event) {
    if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
      return;
    }

    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.ORANGE);
    String message = "";
    if (event.getResult() == PlayerLoginEvent.Result.KICK_BANNED) {
      message = event.getPlayer().getName() + " failed to login as they are banned.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
      message = event.getPlayer().getName() + " failed to login as the server is full.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
      message = event.getPlayer().getName() + " failed to login as the server is whitelisted.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_OTHER) {
      message = event.getPlayer().getName() + " failed to login for an unknown reason.";
    }
    embedBuilder.setAuthor(message, null, DiscordHandler.getInstance().getAvatarUrl(event.getPlayer()));
    embedBuilder.addField("Kick message", ChatUtils.componentToString(event.kickMessage()), false);
    embedBuilder.setFooter("AsyncPlayerPreLoginEvent result: " + event.getResult());
    DiscordHandler.getInstance().sendEmbed(loggingChannel, embedBuilder);
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void BalanceTopUpdateEvent(@NotNull BalanceTopUpdateEvent event) {
    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.ORANGE);
    embedBuilder.setAuthor("Balance top updated in " + event.getTimeTaken() + "ms");
    embedBuilder.setFooter("BalanceTopUpdateEvent");
    DiscordHandler.getInstance().sendEmbed(loggingChannel, embedBuilder);
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void SkillLevelUpEvent(@NotNull SkillLevelUpEvent event) {
    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.GREEN);
    String playerName = Bukkit.getOfflinePlayer(event.getUuid()).getName();
    embedBuilder.setAuthor("Skill level up.", null, DiscordHandler.getInstance().getAvatarUrl(Bukkit.getOfflinePlayer(event.getUuid())));
    
    embedBuilder.addField("Player", String.valueOf(playerName), false);
    embedBuilder.addField("Skill", event.getSkillType().getName(), false);
    embedBuilder.addField("New level", String.valueOf(event.getLevel()), false);
    
    embedBuilder.setFooter("SkillLevelUpEvent");
    
    DiscordHandler.getInstance().sendEmbed(loggingChannel, embedBuilder);
  }
  
}
