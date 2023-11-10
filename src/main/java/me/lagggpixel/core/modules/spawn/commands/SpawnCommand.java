package me.lagggpixel.core.modules.spawn.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.CommandUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SpawnCommand extends CommandClass {
  
  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;
  
  public SpawnCommand(SpawnModule spawnModule, SpawnManager spawnManager) {
    this.spawnModule = spawnModule;
    this.spawnManager = spawnManager;
  }
  
  @Override
  public String getCommandName() {
    return "spawn";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of();
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(spawnModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      if (args.length == 1) {
        Player target = commandSender.getServer().getPlayer(args[0]);
        if (target == null) {
          commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
          return true;
        }
        
        if (spawnManager.getSpawnLocation() == null) {
          commandSender.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
          return true;
        }
        target.teleport(spawnManager.getSpawnLocation());
        target.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of("%name%", Lang.SPAWN_NAME.getDef())));
        commandSender.sendMessage(Lang.SPAWN_TELEPORTED_OTHER.toComponentWithPrefix(Map.of("%player%", target.getName())));
        return true;
      }
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    if (args.length == 0) {
      if (spawnManager.getSpawnLocation() == null) {
        sender.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
        return true;
      }
      
      TeleportUtils.teleportWithDelay(sender, spawnManager.getSpawnLocation(), Lang.SPAWN_NAME.getDef());
      return true;
    }
    
    if (args.length == 1) {
      // Todo - add permission
      if (!sender.hasPermission("placeholder_permission")) {
        sender.sendMessage(Main.getInstance().getServer().permissionMessage());
        return true;
      }
      Player target = sender.getServer().getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        return true;
      }
      
      if (spawnManager.getSpawnLocation() == null) {
        sender.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
        return true;
      }
      target.teleport(spawnManager.getSpawnLocation());
      target.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of("%name%", Lang.SPAWN_NAME.getDef())));
      sender.sendMessage(Lang.SPAWN_TELEPORTED_OTHER.toComponentWithPrefix(Map.of("%player%", target.getName())));
      return true;
    }
    
    sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    return true;
  }
  
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}

