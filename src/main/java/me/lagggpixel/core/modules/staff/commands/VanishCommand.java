package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.handlers.VanishHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class VanishCommand implements ICommandClass {
  
  private final StaffModule module;
  private final VanishHandler vanishHandler;
  
  public VanishCommand(StaffModule module) {
    this.module = module;
    this.vanishHandler = new VanishHandler();
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
            vanishHandler.showPlayer(commandSender, target);
          } else {
            vanishHandler.vanishPlayer(commandSender, target);
          }
        } else {
          commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        }
      }

      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    User senderUser = Main.getUser(sender.getUniqueId());
    if (args.length == 0) {
      if (senderUser.isVanished()) {
        vanishHandler.showPlayer(sender);
      } else {
        vanishHandler.vanishPlayer(sender);
      }
    } else if (args.length == 1) {
      
      Player target = Bukkit.getPlayer(args[0]);
      if (target != null) {
        User user = Main.getUser(target.getUniqueId());
        if (user.isVanished()) {
          vanishHandler.showPlayer(sender, target);
        } else {
          vanishHandler.vanishPlayer(sender, target);
        }
      } else {
        senderUser.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
      }
    } else {
      senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    }
    return true;
  }
  
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
  
}
