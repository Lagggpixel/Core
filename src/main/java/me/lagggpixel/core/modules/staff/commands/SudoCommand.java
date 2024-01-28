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

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.utils.CommandUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class SudoCommand implements ICommandClass {
  
  private final StaffModule staffModule;
  
  public SudoCommand(StaffModule staffModule) {
    this.staffModule = staffModule;
  }
  
  @Override
  public String getCommandName() {
    return "sudo";
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
    return CommandUtils.generateCommandBasePermission(staffModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (args.length <= 1) {
      commandSender.sendMessage(Lang.STAFF_SUDO_USAGE.toComponentWithPrefix());
      return true;
    }
    String targetName = args[0];
    Player target = Bukkit.getPlayerExact(targetName);
    String commandToRun = StringUtils.join(args, ' ', 1, args.length);
    if (targetName.equalsIgnoreCase("*")) {
      if (commandToRun.startsWith("/")) {
        String format = commandToRun.replace("/", "");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.performCommand(format);
        }
        commandSender.sendMessage(Lang.STAFF_SUDO_COMMAND_SUCCESS.toComponentWithPrefix(Map.of(
            "%command%", commandToRun,
            "%players%", "everyone"
        )));
      } else {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          onlinePlayer.chat(commandToRun);
        }
        commandSender.sendMessage(Lang.STAFF_SUDO_CHAT_SUCCESS.toComponentWithPrefix(Map.of(
            "%message%", commandToRun,
            "%players%", "everyone"
        )));
      }
    } else if (commandToRun.startsWith("/")) {
      String format = commandToRun.replace("/", "");
      if (target != null) {
        target.performCommand(format);
        commandSender.sendMessage(Lang.STAFF_SUDO_COMMAND_SUCCESS.toComponentWithPrefix(Map.of(
            "%command%", commandToRun,
            "%players%", targetName
        )));
      } else {
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", targetName)));
      }
    } else if (target != null) {
      commandSender.sendMessage(Lang.STAFF_SUDO_CHAT_SUCCESS.toComponentWithPrefix(Map.of(
          "%message%", commandToRun,
          "%players%", targetName
      )));
      target.chat(commandToRun);
    } else {
      commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", targetName)));
    }
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
