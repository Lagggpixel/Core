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
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class MerchantListSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantListSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 1) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    sender.sendMessage(Lang.PREFIX.toComponent().append(ChatUtils.stringToComponentCC("Merchants:")));
    
    merchantModule.getMerchantHandler().getMerchants().forEach((merchantId, merchant) -> {
      sender.sendMessage(merchant.getId() + ": " + merchant.getName());
    });
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
