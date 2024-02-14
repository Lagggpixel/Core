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

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class Listeners implements Listener {

  public Listeners() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.join")) {
      DiscordHandler.getInstance()
          .sendEmbed(DiscordHandler.getInstance().MESSAGING_CHANNEL,
              DiscordHandler.getInstance().createJoinMessageEmbed(event));
    }

    DiscordModule.getServerStatusHandler().updateServerMessageChannel();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.leave")) {
      DiscordHandler.getInstance()
          .sendEmbed(DiscordHandler.getInstance().MESSAGING_CHANNEL,
              DiscordHandler.getInstance().createQuitMessageEmbed(event));
    }

    DiscordModule.getServerStatusHandler().updateServerMessageChannel();
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerDeathEvent(@NotNull PlayerDeathEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.death")) {
      DiscordHandler.getInstance()
          .sendEmbed(DiscordHandler.getInstance().MESSAGING_CHANNEL,
              DiscordHandler.getInstance().createDeathMessageEmbed(event));
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void AsyncChatEvent(@NotNull AsyncChatEvent event) {
    DiscordHandler.getInstance().handleAsyncChatEvent(event);
  }
}
