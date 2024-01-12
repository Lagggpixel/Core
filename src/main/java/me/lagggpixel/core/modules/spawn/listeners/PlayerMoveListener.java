package me.lagggpixel.core.modules.spawn.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class PlayerMoveListener implements Listener {
  
  private final SpawnModule spawnModule;
  
  public PlayerMoveListener(SpawnModule spawnModule) {
    this.spawnModule = spawnModule;
    Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void PlayerMoveEvent(PlayerMoveEvent event) {
    if (!(event.getTo().getWorld() == spawnModule.getSpawnManager().getSpawnLocation().getWorld())) {
      return;
    }
    if (event.getTo().getBlockY() > 0) {
      return;
    }
    event.getPlayer().teleport(spawnModule.getSpawnManager().getSpawnLocation());
    event.getPlayer().sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of("%name%", "spawn")));
  }
}
