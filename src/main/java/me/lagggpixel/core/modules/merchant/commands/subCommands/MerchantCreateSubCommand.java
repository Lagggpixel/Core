/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.utils.MerchantModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class MerchantCreateSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantCreateSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 2) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    String uniqueId = args[1];
    if (merchantModule.getMerchantHandler().getMerchant(uniqueId) != null) {
      commandSender.sendMessage(Lang.MERCHANT_ALREADY_EXISTS.toComponentWithPrefix(Map.of("merchant", uniqueId)));
      return;
    }
    
    merchantModule.getMerchantHandler().createMerchant(uniqueId, sender.getLocation());
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
