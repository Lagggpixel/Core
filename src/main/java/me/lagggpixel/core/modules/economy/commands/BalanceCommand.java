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
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
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
public class BalanceCommand implements ICommandClass {
  private final EconomyModule economyModule;
  private final EconomyManager economyManager;
  
  public BalanceCommand(EconomyModule economyModule, EconomyManager economyManager) {
    this.economyManager = economyManager;
    this.economyModule = economyModule;
  }
  
  @Override
  public String getCommandName() {
    return "balance";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("balance", "bal");
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
    if (args.length > 1) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    Player targetPlayer;
    if (args.length == 1) {
      if (!commandSender.hasPermission("core.economy.balance.others")) {
        commandSender.sendMessage(ChatUtils.stringToComponentCC(ChatUtils.componentToString(Main.getInstance().getServer().permissionMessage())));
        return true;
      }
      
      targetPlayer = Bukkit.getPlayer(args[0]);
      if (targetPlayer == null) {
        commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        return true;
      }
    } else {
      if (!(commandSender instanceof Player)) {
        commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        return true;
      }
      
      targetPlayer = (Player) commandSender;
    }
    
    double balance = economyManager.getBalance(targetPlayer);
    
    if (targetPlayer.equals(commandSender)) {
      commandSender.sendMessage(Lang.ECONOMY_BALANCE_SELF.toComponentWithPrefix(Map.of("%balance%", String.valueOf(balance))));
      return true;
    }
    else{
      commandSender.sendMessage(Lang.ECONOMY_BALANCE_OTHER.toComponentWithPrefix(Map.of("%player%", targetPlayer.getName(), "%balance%", String.valueOf(balance))));
    }
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
