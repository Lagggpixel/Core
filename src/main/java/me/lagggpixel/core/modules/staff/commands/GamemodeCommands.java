package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.data.CommandClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GamemodeCommands extends CommandClass {
  @Override
  public String getCommandName() {
    return "gamemode";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of(
        "gamemode", "gm",
        "gma", "gamemodeadventure", "gmadventure", "gamemodea",
        "gmc", "gamemodecreative", "gmcreative", "gamemodec",
        "gmsp", "gamemodespectator", "gmspectator", "gamemodesp",
        "gms", "gamemodesurvival", "gmsurvival", "gamemodes"
    );
  }
  
  @Override
  public String getCommandPermission() {
    return "core.staff.command.gamemode";
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    
    if (commandSender instanceof Player player) {
    
    }
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
