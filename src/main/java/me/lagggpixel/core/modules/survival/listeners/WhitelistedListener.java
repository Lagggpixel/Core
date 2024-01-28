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

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class WhitelistedListener implements Listener {
  
  public WhitelistedListener() {
    Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      if (SpawnModule.getInstance().getSpawnManager().getSpawnLocation() != null) {
        event.getPlayer().teleport(SpawnModule.getInstance().getSpawnManager().getSpawnLocation());
      }
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_INFORM.toComponentWithPrefix());
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void asyncCommandExecute(@NotNull PlayerCommandPreprocessEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_ERROR.toComponentWithPrefix());
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_ERROR.toComponentWithPrefix());
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerChat(@NotNull AsyncChatEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_ERROR.toComponentWithPrefix());
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerMove(@NotNull PlayerMoveEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_ERROR.toComponentWithPrefix());
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.SURVIVAL_WHITELISTED_ERROR.toComponentWithPrefix());
    }
  }
}
