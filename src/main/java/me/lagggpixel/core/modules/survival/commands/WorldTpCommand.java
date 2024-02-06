/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.survival.SurvivalModule;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since February 06, 2024
 */
public class WorldTpCommand implements ICommandClass {

  private final SurvivalModule module;

  public WorldTpCommand(SurvivalModule module) {
    this.module = module;
  }

  @Override
  public String getCommandName() {
    return "worldtp";
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
    return CommandUtils.generateCommandBasePermission(module, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

    if (args.length != 2) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }

    Player player = Bukkit.getPlayerExact(args[0]);

    if (player == null) {
      commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of(
          "%player%", args[0])));
      return true;
    }

    World world = Bukkit.getWorld(args[1]);
    if (world == null) {
      commandSender.sendMessage(Lang.WORLD_NOT_FOUND.toComponentWithPrefix(Map.of(
          "%world%", args[1])));
      return true;
    }

    User user = Main.getUser(player.getUniqueId());
    if (user.getWorldData().containsKey(world.getName())) {
      player.teleport(user.getWorldData().get(world.getName()).getLocation());
      return true;
    }
    else {
      player.teleport(world.getSpawnLocation());
      return true;
    }
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

    if (args.length== 0 || args.length == 1) {
      return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
    if (args.length == 2) {
      return Bukkit.getWorlds().stream().map(World::getName).toList();
    }
    return List.of(" ");
  }
}
