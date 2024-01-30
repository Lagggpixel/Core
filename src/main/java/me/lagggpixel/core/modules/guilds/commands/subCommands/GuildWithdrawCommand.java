/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildWithdrawCommand implements ISubCommand {
  
  private final GuildModule guildModule;
  
  public GuildWithdrawCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  public void execute(CommandSender commandSender, String[] args) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    User senderUser = Main.getUser(sender.getUniqueId());
    
    Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);
    
    if (guild == null) {
      senderUser.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }
    
    if (!guild.isOfficer(sender) && !guild.isLeader(sender.getUniqueId())) {
      senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
      return;
    }
    if (args.length == 0) {
      senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    if (!NumberUtils.isNumber(args[0])) {
      if (args[0].equalsIgnoreCase("all")) {
        
        if (guild.getBalance() <= 0) {
          senderUser.sendMessage(Lang.GUILD_WITHDRAW_ECONOMY_BROKE.toComponentWithPrefix());
          return;
        }
        senderUser.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", guild.getBalance() + "")));
        guild.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", guild.getBalance() + "")));
        EconomyManager.getInstance().deposit(sender, guild.getBalance());
        guild.setBalance(0);
        return;
      }
      senderUser.sendMessage(Lang.ECONOMY_INVALID_AMOUNT.toComponentWithPrefix());
      return;
    }
    double amount = Double.parseDouble(args[0]);
    if (amount <= 0) {
      senderUser.sendMessage(Lang.GUILD_WITHDRAW_NEGATIVE.toComponentWithPrefix());
      
      return;
    }
    if (guild.getBalance() < amount) {
      senderUser.sendMessage(Lang.GUILD_WITHDRAW_NOT_ENOUGH.toComponentWithPrefix());
      
      return;
    }
    EconomyManager.getInstance().deposit(sender, amount);
    senderUser.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", amount + "")));
    guild.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", amount + "")));
    guild.setBalance(guild.getBalance() - amount);
    
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of("all");
  }
  
}
