/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.chat.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class StaffChatHandler {
  
  public static void sendStaffChatMessage(Player sender, Component message) {
    User user = Main.getUser(sender.getUniqueId());
    Component messageToSend = Lang.CHAT_STAFF_CHAT.toComponent(Map.of(
        "%sender%", sender.getName(),
        "%message%", ChatUtils.componentToString(message)));
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission("coreplugin.chat.command.player.staffchat.view")) {
        user.sendMessage(messageToSend);
      }
    });
  }
  
}
