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
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GuildDepositCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildDepositCommand(GuildModule guildModule) {
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

    if (args.length == 0) {
      senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    User user = Main.getUser(sender.getUniqueId());

    if (!NumberUtils.isNumber(args[0])) {
      if (args[0].equalsIgnoreCase("all")) {

        double balance = user.getPlayerBalance();

        if (balance <= 0) {
          senderUser.sendMessage(Lang.GUILD_DEPOSIT_NEGATIVE.toComponentWithPrefix());
          return;
        }

        guild.setBalance(guild.getBalance() + balance);
        senderUser.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", balance + "")));
        guild.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", balance + "")));
        EconomyManager.getInstance().withdraw(sender, balance);
        return;
      }
      senderUser.sendMessage(Lang.ECONOMY_INVALID_AMOUNT.toComponentWithPrefix());

      return;
    }
    double amount = Double.parseDouble(args[0]);
    if (amount <= 0) {
      senderUser.sendMessage(Lang.GUILD_DEPOSIT_NEGATIVE.toComponentWithPrefix());
      return;
    }
    if (EconomyManager.getInstance().getBalance(sender) < amount) {
      senderUser.sendMessage(Lang.ECONOMY_NOT_ENOUGH_MONEY.toComponentWithPrefix());
      return;
    }
    EconomyManager.getInstance().withdraw(sender, amount);
    guild.setBalance(guild.getBalance() + amount);
    senderUser.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", amount + "")));
    guild.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", amount + "")));
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of("all");
  }
}
