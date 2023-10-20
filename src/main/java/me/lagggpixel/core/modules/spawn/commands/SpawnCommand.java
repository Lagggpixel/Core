package me.lagggpixel.core.modules.spawn.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnCommand extends CommandClass {
  
  SpawnManager spawnManager;
  
  public SpawnCommand(SpawnManager spawnManager) {
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
    return "core.command.spawn";
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
    if (!(commandSender instanceof Player)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
      return true;
    }
    
    Player sender = (Player) commandSender;
    
    if (args.length == 0) {
      if (spawnManager.getSpawnLocation() == null) {
        sender.sendMessage(Lang.SPAWN_NO_SET_SPAWN.toComponentWithPrefix(null));
        return true;
      }
      
      TeleportUtils.teleportWithDelay(sender, spawnManager.getSpawnLocation(), Lang.SPAWN_NAME.getDef());
    }
    
    return true;
  }
  
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}

