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
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class PlayerStatsListeners implements Listener {

  public PlayerStatsListeners() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockBreak(@NotNull BlockBreakEvent event) {
    User user = Main.getUser(event.getPlayer().getUniqueId());
    Material material = event.getBlock().getType();

    if (user.getUserStats().getBlocksBroken().containsKey(material)) {
      user.getUserStats().getBlocksBroken().replace(material, user.getUserStats().getBlocksBroken().get(material) + 1);
    } else {
      user.getUserStats().getBlocksBroken().put(material, 1L);
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockPlace(@NotNull BlockPlaceEvent event) {
    User user = Main.getUser(event.getPlayer().getUniqueId());
    Material material = event.getBlock().getType();

    if (user.getUserStats().getBlocksPlaced().containsKey(material)) {
      user.getUserStats().getBlocksPlaced().replace(material, user.getUserStats().getBlocksPlaced().get(material) + 1);
    } else {
      user.getUserStats().getBlocksPlaced().put(material, 1L);
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEntityKill(@NotNull EntityDeathEvent event) {
    if ((event.getEntity().getKiller() == null) || !event.getEntity().isDead()) {
      return;
    }

    User user = Main.getUser(event.getEntity().getKiller().getUniqueId());
    EntityType entityType = event.getEntity().getType();

    if (user.getUserStats().getEntityKills().containsKey(entityType)) {
      user.getUserStats().getEntityKills().replace(entityType, user.getUserStats().getEntityKills().get(entityType) + 1);
    } else {
      user.getUserStats().getEntityKills().put(entityType, 1L);
    }
  }

}
