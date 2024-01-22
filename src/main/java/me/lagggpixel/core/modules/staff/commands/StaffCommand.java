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
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.handlers.StaffModeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class StaffCommand implements ICommandClass {
  private final StaffModule staffModule;
  private final StaffModeHandler staffModeHandler = new StaffModeHandler();
  
  public StaffCommand(StaffModule staffModule) {
    this.staffModule = staffModule;
  }
  
  @Override
  public String getCommandName() {
    return "staff";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("staff", "staffmode", "admin");
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
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return true;
    }
    User senderUser = Main.getUser(sender.getUniqueId());
    if (args.length == 0) {
      boolean newStaffModeState = !senderUser.isStaffMode();
      senderUser.setStaffMode(newStaffModeState);
      
      if (newStaffModeState) {
        // Player entered staff mode
        senderUser.sendMessage(Lang.STAFF_MODE_ENABLED.toComponentWithPrefix());
        staffModeHandler.enterStaffMode(sender);
      } else {
        // Player exited staff mode
        senderUser.sendMessage(Lang.STAFF_MODE_DISABLED.toComponentWithPrefix());
        staffModeHandler.exitStaffMode(sender);
      }
      return true;
    }
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    // Tab completion logic (if needed)
    return null;
  }
}
