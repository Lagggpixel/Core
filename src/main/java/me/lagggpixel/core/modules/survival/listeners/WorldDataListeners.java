/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.data.user.UserWorldData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Lagggpixel
 * @since February 06, 2024
 */
public class WorldDataListeners implements Listener {

  public WorldDataListeners() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    User user = Main.getUser(player.getUniqueId());
    setUserLastLocation(user, player.getLocation());
  }

  @EventHandler
  public void onPlayerWorldChange(PlayerTeleportEvent event) {
    Player player = event.getPlayer();

    if (event.getFrom().getWorld() == event.getTo().getWorld()) {
      return;
    }

    User user = Main.getUser(player.getUniqueId());
    setUserLastLocation(user, event.getFrom());
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    User user = Main.getUser(player.getUniqueId());
    user.getWorldData().remove(player.getWorld().getName());
  }

  private void setUserLastLocation(User user, Location location) {
    String worldName = location.getWorld().getName();

    if (user.getWorldData().containsKey(worldName)) {
      user.getWorldData().get(worldName).modifyLocation(location);
      return;
    }

    user.getWorldData().put(worldName, new UserWorldData(worldName, location));
  }
}
