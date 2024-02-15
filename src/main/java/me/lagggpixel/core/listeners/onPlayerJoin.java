/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.staff.handlers.VanishHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class onPlayerJoin implements Listener {

  public onPlayerJoin() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    User user = Main.getUser(uuid);
    user.setPlayerName(player.getName());
    user.setAfk(false);

    for (Component component : user.getGetQueuedMessage()) {
      user.sendMessage(component);
    }

    handleVanishPlayers(event);
  }

  private void handleVanishPlayers(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission(VanishHandler.vanishSeePermission)) {
      return;
    }

    Bukkit.getOnlinePlayers().stream()
        .filter(x -> Main.getUser(x.getUniqueId()).isVanished())
        .forEach(onlinePlayer -> player.hidePlayer(Main.getInstance(), onlinePlayer));

  }
}