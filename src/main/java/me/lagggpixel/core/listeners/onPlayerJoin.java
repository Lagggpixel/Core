package me.lagggpixel.core.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class onPlayerJoin implements Listener {
  
  public onPlayerJoin() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (Main.getUser(uuid) == null) {
      Main.getUserData().put(uuid, new User(player));
    }
  }
}
