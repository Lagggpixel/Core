/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.chat.handlers.StaffChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
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
