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

public class WhitelistedListener implements Listener {
  
  public WhitelistedListener() {
    Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    if (Main.getInstance().whitelisted && !event.getPlayer().isOp()) {
      event.getPlayer().teleport(SpawnModule.getInstance().getSpawnManager().getSpawnLocation());
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
