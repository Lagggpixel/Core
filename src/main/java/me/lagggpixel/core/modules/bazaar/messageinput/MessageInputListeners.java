/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.bazaar.messageinput;

import me.lagggpixel.core.modules.bazaar.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @since January 22, 2024
 */
public class MessageInputListeners implements Listener {
  private final MessageInputManager messageInputManager;

  public MessageInputListeners(MessageInputManager messageInputManager) {
    this.messageInputManager = messageInputManager;
  }

  @EventHandler
  public void onMessage(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!messageInputManager.isWaitingForOneLineInput(player)) return;

    messageInputManager.handleInput(player, Utils.colorize(event.getMessage()));
    event.setCancelled(true);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    messageInputManager.removePlayer(event.getPlayer());
  }
}
