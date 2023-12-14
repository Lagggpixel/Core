package me.lagggpixel.core.modules.spawn.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {

  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;

  public onPlayerJoin(SpawnModule spawnModule) {
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
