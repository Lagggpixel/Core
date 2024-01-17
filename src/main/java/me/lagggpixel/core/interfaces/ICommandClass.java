/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.interfaces;

import org.bukkit.command.TabExecutor;

import java.util.List;

public interface ICommandClass extends TabExecutor {
  
  String getCommandName();

  String getCommandDescription();

  List<String> getCommandAliases();

  String getCommandPermission();

  String getUsage();
}
