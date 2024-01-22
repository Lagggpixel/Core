/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.economy.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class BalanceTopCommand implements ICommandClass {

  private final EconomyModule economyModule;
  private final EconomyManager economyManager;

  public BalanceTopCommand(EconomyModule economyModule, EconomyManager economyManager) {
    this.economyModule = economyModule;
    this.economyManager = economyManager;
  }

  @Override
  public String getCommandName() {
    return "balancetop";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of("balancetop", "baltop");
  }

  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(economyModule, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
    if (args.length > 0) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }

    List<Map.Entry<UUID, Double>> balanceTop = economyManager.getBalanceTop();

    if (balanceTop.isEmpty()) {
      commandSender.sendMessage(Lang.ECONOMY_BALTOP_NO_PLAYER_FOUND.toComponentWithPrefix());
      return true;
    }

    commandSender.sendMessage(Lang.ECONOMY_BALTOP_HEADER.toComponent());

    for (int i = 0; i < Math.min(10, balanceTop.size()); i++) {
      Map.Entry<UUID, Double> entry = balanceTop.get(i);
      commandSender.sendMessage(Lang.ECONOMY_BALTOP_LISTING.toComponent(Map.of(
          "%position%", String.valueOf(i + 1),
          "%player%", Main.getUser(entry.getKey()).getPlayerName(),
          "%balance%", String.valueOf(entry.getValue())
      )));
      commandSender.sendMessage((i + 1) + ". " + entry.getKey() + ": " + entry.getValue());
    }

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}