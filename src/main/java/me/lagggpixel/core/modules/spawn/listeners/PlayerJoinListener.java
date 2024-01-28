/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.spawn.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class PlayerJoinListener implements Listener {

  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;

  public PlayerJoinListener(SpawnModule spawnModule) {
    this.spawnModule = spawnModule;
    this.spawnManager = this.spawnModule.getSpawnManager();

    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void PlayerJoinEvent(PlayerJoinEvent event) {
    if (!event.getPlayer().hasPlayedBefore()) {
      Player player = event.getPlayer();
      player.teleport(spawnManager.getSpawnLocation());
    }
  }

}
