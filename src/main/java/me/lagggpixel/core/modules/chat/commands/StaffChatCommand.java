/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.chat.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.chat.ChatModule;
import me.lagggpixel.core.modules.chat.handlers.StaffChatHandler;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class StaffChatCommand implements ICommandClass {
  private final ChatModule module;

  public StaffChatCommand(ChatModule module) {
    this.module = module;
  }

  @Override
  public String getCommandName() {
    return "staffchat";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of("staffchat", "sc");
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
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return true;
    }

    if (args.length == 0) {
      User user = Main.getUser(sender.getUniqueId());
      if (user.isStaffChatToggled()) {
        user.setStaffChatToggled(false);
        user.sendMessage(Lang.CHAT_STAFF_CHAT_TOGGLE_OFF.toComponentWithPrefix());
        return true;
      }
      user.setStaffChatToggled(true);
      user.sendMessage(Lang.CHAT_STAFF_CHAT_TOGGLE_ON.toComponentWithPrefix());
      return true;
    }

    String message = String.join(" ", args);
    StaffChatHandler.sendStaffChatMessage(sender, ChatUtils.stringToComponent(message));

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
