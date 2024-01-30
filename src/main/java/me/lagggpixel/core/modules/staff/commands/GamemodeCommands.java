/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
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

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GamemodeCommands implements ICommandClass {
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
      User senderUser = Main.getUser(sender.getUniqueId());
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
            senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(Map.of("%gamemode%", s)));
          }
        }
        return true;
      }

      if (args.length == 1) {
        if (!(s.equalsIgnoreCase("gamemode") || s.equalsIgnoreCase("gm"))) {
          Player target = sender.getServer().getPlayer(args[0]);
          if (target == null) {
            senderUser.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
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
            default -> senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          }
          return true;
        }

        switch (args[0].toLowerCase()) {
          case "s", "survival", "0" -> gamemodeHandler.setGameMode(sender, GameMode.SURVIVAL);
          case "c", "creative", "1" -> gamemodeHandler.setGameMode(sender, GameMode.CREATIVE);
          case "a", "adventure", "2" -> gamemodeHandler.setGameMode(sender, GameMode.ADVENTURE);
          case "sp", "spectator", "3" -> gamemodeHandler.setGameMode(sender, GameMode.SPECTATOR);
          default ->
              senderUser.sendMessage(Lang.STAFF_GAMEMODE_INVALID.toComponentWithPrefix(Map.of("%gamemode%", args[0])));
        }
        return true;
      }

      if (args.length == 2) {
        if (!(s.equalsIgnoreCase("gamemode") || s.equalsIgnoreCase("gm"))) {
          senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          return true;
        }

        Player target = sender.getServer().getPlayer(args[1]);
        if (target == null) {
          senderUser.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[1])));
          return true;
        }

        switch (args[0].toLowerCase()) {
          case "s", "survival", "0" -> gamemodeHandler.setGameMode(sender, target, GameMode.SURVIVAL);
          case "c", "creative", "1" -> gamemodeHandler.setGameMode(sender, target, GameMode.CREATIVE);
          case "a", "adventure", "2" -> gamemodeHandler.setGameMode(sender, target, GameMode.ADVENTURE);
          case "sp", "spectator", "3" -> gamemodeHandler.setGameMode(sender, target, GameMode.SPECTATOR);
          default ->
              senderUser.sendMessage(Lang.STAFF_GAMEMODE_INVALID.toComponentWithPrefix(Map.of("%gamemode%", args[0])));
        }
        return true;
      }

      senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }

    if (args.length == 1) {
      Player target = commandSender.getServer().getPlayer(args[0]);
      if (target == null) {
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
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
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[1])));
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
