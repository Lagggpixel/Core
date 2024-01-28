/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class GamemodeHandler {
  
  public void setGameMode(Player player, GameMode gamemode) {
    User user = Main.getUser(player.getUniqueId());
    player.setGameMode(gamemode);
    user.sendMessage(Lang.STAFF_GAMEMODE_SELF.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase())));
  }
  
  public void setGameMode(Player sender, Player target, GameMode gamemode) {
    User senderUser = Main.getUser(sender.getUniqueId());
    User targetUser = Main.getUser(target.getUniqueId());
    target.setGameMode(gamemode);
    senderUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", target.getName())));
    targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", sender.getName())));
  }
  
  public void setGameMode(CommandSender commandSender, Player target, GameMode gamemode) {
    User targetUser = Main.getUser(target.getUniqueId());
    target.setGameMode(gamemode);
    commandSender.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", target.getName())));
    if (commandSender instanceof ConsoleCommandSender) {
      targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", "console")));
    }
    else {
      targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", "unknown")));
    }
  }
  
}
