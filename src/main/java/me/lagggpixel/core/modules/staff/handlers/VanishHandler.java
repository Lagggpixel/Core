package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class VanishHandler {
  public static final String vanishSeePermission = "coreplugin.staff.command.vanish.see";
  
  public void showPlayer(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), player);
    });
    player.sendMessage(Lang.STAFF_UNVANISHED_SELF.toComponentWithPrefix());
  }
  
  public void vanishPlayer(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), player);
      }
    });
    player.sendMessage(Lang.STAFF_VANISHED_SELF.toComponentWithPrefix());
  }
  
  
  public void showPlayer(Player sender, Player target) {
    User user = Main.getUser(target.getUniqueId());
    user.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), target);
    });
    sender.sendMessage(Lang.STAFF_UNVANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    target.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", sender.getName())));
  }
  
  public void vanishPlayer(Player sender, Player target) {
    User user = Main.getUser(target.getUniqueId());
    user.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), target);
      }
    });
    sender.sendMessage(Lang.STAFF_VANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    target.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", sender.getName())));
  }
  
  public void showPlayer(CommandSender sender, Player target) {
    User user = Main.getUser(target.getUniqueId());
    user.setVanished(false);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      onlinePlayer.showPlayer(Main.getInstance(), target);
    });
    sender.sendMessage(Lang.STAFF_UNVANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    if (sender instanceof ConsoleCommandSender) {
      target.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "console")));
    }
    else {
      target.sendMessage(Lang.STAFF_UNVANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "unknown")));
      
    }
  }
  
  public void vanishPlayer(CommandSender sender, Player target) {
    User user = Main.getUser(target.getUniqueId());
    user.setVanished(true);
    Main.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
      if (onlinePlayer.hasPermission(vanishSeePermission)) {
        onlinePlayer.hidePlayer(Main.getInstance(), target);
      }
    });
    sender.sendMessage(Lang.STAFF_VANISHED_OTHER.toComponentWithPrefix(Map.of("%%player%", target.getName())));
    if (sender instanceof ConsoleCommandSender) {
      target.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "console")));
    }
    else {
      target.sendMessage(Lang.STAFF_VANISHED_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%%player%", "unknown")));
    }
  }
  
}
