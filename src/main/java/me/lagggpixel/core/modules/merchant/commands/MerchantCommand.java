/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.merchant.commands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.modules.merchant.commands.subCommands.*;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class MerchantCommand implements ICommandClass {

  private final MerchantModule merchantModule;
  private final Map<String, ISubCommand> subCommands;

  public MerchantCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
    this.subCommands = new HashMap<>();

    subCommands.put("create", new MerchantCreateSubCommand(merchantModule));
    subCommands.put("delete", new MerchantDeleteSubCommand(merchantModule));
    subCommands.put("item", new MerchantItemSubCommand(merchantModule));
    subCommands.put("list", new MerchantListSubCommand(merchantModule));
    subCommands.put("rename", new MerchantRenameSubCommand(merchantModule));
    subCommands.put("select", new MerchantSelectSubCommand(merchantModule));
    subCommands.put("skin", new MerchantSkinSubCommand(merchantModule));
    subCommands.put("tphere", new MerchantTphereSubCommand(merchantModule));
  }


  @Override
  public String getCommandName() {
    return "merchant";
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
    return CommandUtils.generateCommandBasePermission(merchantModule, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (args.length == 0) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }

    String subCommand = args[0].toLowerCase();

    if (subCommands.containsKey(subCommand)) {
      subCommands.get(subCommand).execute(commandSender, args);
      return true;
    }

    commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (args.length == 1) {
      return subCommands.keySet().stream().toList();
    }

    String subCommand = args[0].toLowerCase();

    if (!subCommands.containsKey(subCommand)) {
      return List.of(" ");
    }

    return subCommands.get(subCommand).tabComplete(commandSender, args);
  }
}
