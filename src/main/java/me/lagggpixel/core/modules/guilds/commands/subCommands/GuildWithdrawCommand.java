package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

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
    
    Guild faction = guildModule.getGuildHandler().getGuildFromPlayer(sender);
    
    if (faction == null) {
      sender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      
      return;
    }
    
    if (!faction.isOfficer(sender) && !faction.isLeader(sender.getUniqueId())) {
      sender.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
      
      return;
    }
    if (args.length == 0) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    if (!NumberUtils.isNumber(args[0])) {
      if (args[0].equalsIgnoreCase("all")) {
        
        if (faction.getBalance() <= 0) {
          sender.sendMessage(Lang.GUILD_WITHDRAW_ECONOMY_BROKE.toComponentWithPrefix());
          return;
        }
        sender.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", faction.getBalance() + "")));
        faction.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", faction.getBalance() + "")));
        EconomyManager.getInstance().deposit(sender, faction.getBalance());
        faction.setBalance(0);
        return;
      }
      sender.sendMessage(Lang.ECONOMY_INVALID_AMOUNT.toComponentWithPrefix());
      return;
    }
    double amount = Double.parseDouble(args[0]);
    if (amount <= 0) {
      sender.sendMessage(Lang.GUILD_WITHDRAW_NEGATIVE.toComponentWithPrefix());
      
      return;
    }
    if (faction.getBalance() < amount) {
      sender.sendMessage(Lang.GUILD_WITHDRAW_NOT_ENOUGH.toComponentWithPrefix());
      
      return;
    }
    EconomyManager.getInstance().deposit(sender, amount);
    sender.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", amount + "")));
    faction.sendMessage(Lang.GUILD_WITHDRAW_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", amount + "")));
    faction.setBalance(faction.getBalance() - amount);
    
  }
  
}
