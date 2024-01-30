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

import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
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
public class SetSpawnCommand implements ICommandClass {

  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;

  public SetSpawnCommand(SpawnModule spawnModule) {
    this.spawnModule = spawnModule;
    this.spawnManager = this.spawnModule.getSpawnManager();
  }

  @Override
  public String getCommandName() {
    return "setspawn";
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
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return true;
    }

    spawnManager.setSpawnLocation(sender.getLocation());
    spawnManager.saveSpawnLocation();

    commandSender.sendMessage(Lang.SPAWN_SUCCESSFULLY_SET.toComponentWithPrefix());

    return true;


  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
