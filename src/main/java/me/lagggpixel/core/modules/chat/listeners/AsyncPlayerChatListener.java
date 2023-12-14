package me.lagggpixel.core.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.chat.handlers.StaffChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {
  
  public AsyncPlayerChatListener() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
  public void AsyncPlayerChatEvent(AsyncChatEvent event) {
    Player player = event.getPlayer();
    User user = Main.getUser(player.getUniqueId());
    
    if (user.isStaffChatToggled()) {
      StaffChatHandler.sendStaffChatMessage(player, event.message());
      event.setCancelled(true);
      return;
    }

    // TODO: block report message (un-sign message)

    // TODO: add pretty message printing with prefix

  }
  
}
