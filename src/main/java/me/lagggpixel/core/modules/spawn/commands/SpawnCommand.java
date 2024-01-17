/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.spawn.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SpawnCommand implements ICommandClass {
  
  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;
  
  public SpawnCommand(SpawnModule spawnModule) {
    this.spawnModule = spawnModule;
    this.spawnManager =  this.spawnModule.getSpawnManager();
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
        
        User targetUser = Main.getUser(target.getUniqueId());
        
        if (spawnManager.getSpawnLocation() == null) {
          commandSender.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
          return true;
        }
        target.teleport(spawnManager.getSpawnLocation());
        targetUser.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of("%name%", Lang.SPAWN_NAME.getDef())));
        commandSender.sendMessage(Lang.SPAWN_TELEPORTED_OTHER.toComponentWithPrefix(Map.of("%player%", target.getName())));
        return true;
      }
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    User senderUser = Main.getUser(sender.getUniqueId());
    
    if (args.length == 0) {
      if (spawnManager.getSpawnLocation() == null) {
        senderUser.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
        return true;
      }
      
      TeleportUtils.teleportWithDelay(sender, spawnManager.getSpawnLocation(), Lang.SPAWN_NAME.getDef());
      return true;
    }

    if (args.length == 1) {
      if (!sender.hasPermission("core.spawn.spawn.others")) {
        senderUser.sendMessage(ChatUtils.stringToComponentCC(ChatUtils.componentToString(Main.getInstance().getServer().permissionMessage())));
        return true;
      }
      Player target = sender.getServer().getPlayer(args[0]);
      if (target == null) {
        senderUser.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        return true;
      }
      
      User targetUser = Main.getUser(target.getUniqueId());

      if (spawnManager.getSpawnLocation() == null) {
        senderUser.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix());
        return true;
      }
      target.teleport(spawnManager.getSpawnLocation());
      targetUser.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of("%name%", Lang.SPAWN_NAME.getDef())));
      senderUser.sendMessage(Lang.SPAWN_TELEPORTED_OTHER.toComponentWithPrefix(Map.of("%player%", target.getName())));
      return true;
    }
    
    senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    return true;
  }
  
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}

