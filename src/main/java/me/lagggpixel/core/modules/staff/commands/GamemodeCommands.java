package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.handlers.GamemodeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class GamemodeCommands extends CommandClass {
  private final StaffModule module;
  private final GamemodeHandler gamemodeHandler;
  
  public GamemodeCommands(StaffModule module) {
    this.module = module;
    this.gamemodeHandler = new GamemodeHandler();
  }
  
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
    return CommandUtils.generateCommandBasePermission(module, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    
    if (commandSender instanceof Player sender) {
      if (args.length == 0) {
        switch (s) {
          case "gma", "gamemodeadventure", "gmadventure", "gamemodea" ->
              gamemodeHandler.setGameMode(sender, GameMode.ADVENTURE);
          case "gmc", "gamemodecreative", "gmcreative", "gamemodec" ->
              gamemodeHandler.setGameMode(sender, GameMode.CREATIVE);
          case "gmsp", "gamemodespectator", "gmspectator", "gamemodesp" ->
              gamemodeHandler.setGameMode(sender, GameMode.SPECTATOR);
          case "gms", "gamemodesurvival", "gmsurvival", "gamemodes" ->
              gamemodeHandler.setGameMode(sender, GameMode.SURVIVAL);
          default -> {
            sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          }
        }
        return true;
      }
      
      if (args.length == 1) {
        if (!(s.equalsIgnoreCase("gamemode") || s.equalsIgnoreCase("gm"))) {
          Player target = sender.getServer().getPlayer(args[0]);
          if (target == null) {
            sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
            return true;
          }
          switch (s) {
            case "gma", "gamemodeadventure", "gmadventure", "gamemodea" ->
                gamemodeHandler.setGameMode(sender, target, GameMode.ADVENTURE);
            case "gmc", "gamemodecreative", "gmcreative", "gamemodec" ->
                gamemodeHandler.setGameMode(sender, target, GameMode.CREATIVE);
            case "gmsp", "gamemodespectator", "gmspectator", "gamemodesp" ->
                gamemodeHandler.setGameMode(sender, target, GameMode.SPECTATOR);
            case "gms", "gamemodesurvival", "gmsurvival", "gamemodes" ->
                gamemodeHandler.setGameMode(sender, target, GameMode.SURVIVAL);
            default -> sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          }
          return true;
        }
        
        switch (args[0].toLowerCase()) {
          case "s", "survival", "0" -> gamemodeHandler.setGameMode(sender, GameMode.SURVIVAL);
          case "c", "creative", "1" -> gamemodeHandler.setGameMode(sender, GameMode.CREATIVE);
          case "a", "adventure", "2" -> gamemodeHandler.setGameMode(sender, GameMode.ADVENTURE);
          case "sp", "spectator", "3" -> gamemodeHandler.setGameMode(sender, GameMode.SPECTATOR);
          default ->
              sender.sendMessage(Lang.STAFF_GAMEMODE_INVALID.toComponentWithPrefix(Map.of("%gamemode%", args[0])));
        }
        return true;
      }
      
      if (args.length == 2) {
        if (!(s.equalsIgnoreCase("gamemode") || s.equalsIgnoreCase("gm"))) {
          sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          return true;
        }
        
        Player target = sender.getServer().getPlayer(args[1]);
        if (target == null) {
          sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
          return true;
        }
        
        switch (args[0].toLowerCase()) {
          case "s", "survival", "0" -> gamemodeHandler.setGameMode(sender, target, GameMode.SURVIVAL);
          case "c", "creative", "1" -> gamemodeHandler.setGameMode(sender, target, GameMode.CREATIVE);
          case "a", "adventure", "2" -> gamemodeHandler.setGameMode(sender, target, GameMode.ADVENTURE);
          case "sp", "spectator", "3" -> gamemodeHandler.setGameMode(sender, target, GameMode.SPECTATOR);
          default ->
              sender.sendMessage(Lang.STAFF_GAMEMODE_INVALID.toComponentWithPrefix(Map.of("%gamemode%", args[0])));
        }
        return true;
      }
      
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    if (args.length == 1) {
      Player target = commandSender.getServer().getPlayer(args[0]);
      if (target == null) {
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
        return true;
      }
      switch (s) {
        case "gma", "gamemodeadventure", "gmadventure", "gamemodea" ->
            gamemodeHandler.setGameMode(commandSender, target, GameMode.ADVENTURE);
        case "gmc", "gamemodecreative", "gmcreative", "gamemodec" ->
            gamemodeHandler.setGameMode(commandSender, target, GameMode.CREATIVE);
        case "gmsp", "gamemodespectator", "gmspectator", "gamemodesp" ->
            gamemodeHandler.setGameMode(commandSender, target, GameMode.SPECTATOR);
        case "gms", "gamemodesurvival", "gmsurvival", "gamemodes" ->
            gamemodeHandler.setGameMode(commandSender, target, GameMode.SURVIVAL);
        default -> commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      }
      return true;
    }
    
    if (args.length == 2) {
      if (!(s.equalsIgnoreCase("gamemode") || s.equalsIgnoreCase("gm"))) {
        commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        return true;
      }
      
      Player target = commandSender.getServer().getPlayer(args[1]);
      if (target == null) {
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
        return true;
      }
      
      switch (args[0].toLowerCase()) {
        case "s", "survival", "0" -> gamemodeHandler.setGameMode(commandSender, target, GameMode.SURVIVAL);
        case "c", "creative", "1" -> gamemodeHandler.setGameMode(commandSender, target, GameMode.CREATIVE);
        case "a", "adventure", "2" -> gamemodeHandler.setGameMode(commandSender, target, GameMode.ADVENTURE);
        case "sp", "spectator", "3" -> gamemodeHandler.setGameMode(commandSender, target, GameMode.SPECTATOR);
        default ->
            commandSender.sendMessage(Lang.STAFF_GAMEMODE_INVALID.toComponentWithPrefix(Map.of("%gamemode%", args[0])));
      }
      return true;
    }
    
    commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
  
}
