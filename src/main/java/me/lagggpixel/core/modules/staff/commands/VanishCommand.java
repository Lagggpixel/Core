package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class VanishCommand extends CommandClass {
  
  private final StaffModule module;
  public static final String vanishSeePermission = "coreplugin.staff.command.vanish.see";
  
  public VanishCommand(StaffModule module) {
    this.module = module;
  }
  
  @Override
  public String getCommandName() {
    return "vanish";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("vanish", "v");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(module, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (!(commandSender instanceof Player sender)) {
      if (args.length == 1) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
          User user = Main.getUser(target.getUniqueId());
          if (user.isVanished()) {
            showPlayer(commandSender, target);
          } else {
            vanishPlayer(commandSender, target);
          }
        } else {
          commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        }
      }

      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    if (args.length == 0) {
      User user = Main.getUser(sender.getUniqueId());
      if (user.isVanished()) {
        showPlayer(sender);
      } else {
        vanishPlayer(sender);
      }
    } else if (args.length == 1) {
      Player target = Bukkit.getPlayer(args[0]);
      if (target != null) {
        User user = Main.getUser(target.getUniqueId());
        if (user.isVanished()) {
          showPlayer(sender, target);
        } else {
          vanishPlayer(sender, target);
        }
      } else {
        sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
      }
    } else {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    }
    return true;
  }
  
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
  
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
