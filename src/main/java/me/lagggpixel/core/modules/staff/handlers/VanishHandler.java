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
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class VanishHandler {
  public static final String vanishSeePermission = "coreplugin.staff.command.vanish.see";
  
  public void showPlayer(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), player);
    });
    user.sendMessage(Lang.STAFF_UNVANISHED_SELF.toComponentWithPrefix());
  }
  
  public void vanishPlayer(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), player);
      }
    });
    user.sendMessage(Lang.STAFF_VANISHED_SELF.toComponentWithPrefix());
  }
  
  
  public void showPlayer(Player sender, Player target) {
    User targetUser = Main.getUser(target.getUniqueId());
    User senderUser = Main.getUser(sender.getUniqueId());
    targetUser.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), target);
    });
    senderUser.sendMessage(Lang.STAFF_UNVANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    targetUser.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", sender.getName())));
  }
  
  public void vanishPlayer(Player sender, Player target) {
    User targetUser = Main.getUser(target.getUniqueId());
    User senderUser = Main.getUser(sender.getUniqueId());
    targetUser.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), target);
      }
    });
    senderUser.sendMessage(Lang.STAFF_VANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    targetUser.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", sender.getName())));
  }
  
  public void showPlayer(CommandSender commandSender, Player target) {
    User targetUser = Main.getUser(target.getUniqueId());
    targetUser.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), target);
    });
    commandSender.sendMessage(Lang.STAFF_UNVANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    if (commandSender instanceof ConsoleCommandSender) {
      targetUser.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "console")));
    }
    else {
      targetUser.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "unknown")));
    }
  }
  
  public void vanishPlayer(CommandSender commandSender, Player target) {
    User targetUser = Main.getUser(target.getUniqueId());
    targetUser.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), target);
      }
    });
    commandSender.sendMessage(Lang.STAFF_VANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    if (commandSender instanceof ConsoleCommandSender) {
      targetUser.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "console")));
    }
    else {
      targetUser.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "unknown")));
    }
  }
  
}
