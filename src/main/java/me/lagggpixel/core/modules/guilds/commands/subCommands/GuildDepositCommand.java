package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);

    if (guild == null) {
      sender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    if (args.length == 0) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    User user = Main.getUser(sender.getUniqueId());

    if (!NumberUtils.isNumber(args[0])) {
      if (args[0].equalsIgnoreCase("all")) {

        double balance = user.getPlayerBalance();

        if (balance <= 0) {
          sender.sendMessage(Lang.GUILD_DEPOSIT_NEGATIVE.toComponentWithPrefix());
          return;
        }

        guild.setBalance(guild.getBalance() + balance);
        sender.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", balance + "")));
        guild.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", balance + "")));
        EconomyManager.getInstance().withdraw(sender, balance);
        return;
      }
      sender.sendMessage(Lang.ECONOMY_INVALID_AMOUNT.toComponentWithPrefix());

      return;
    }
    double amount = Double.parseDouble(args[0]);
    if (amount <= 0) {
      sender.sendMessage(Lang.GUILD_DEPOSIT_NEGATIVE.toComponentWithPrefix());
      return;
    }
    if (EconomyManager.getInstance().getBalance(sender) < amount) {
      sender.sendMessage(Lang.ECONOMY_NOT_ENOUGH_MONEY.toComponentWithPrefix());
      return;
    }
    EconomyManager.getInstance().withdraw(sender, amount);
    guild.setBalance(guild.getBalance() + amount);
    sender.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%amount%", amount + "")));
    guild.sendMessage(Lang.GUILD_DEPOSIT_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%amount%", amount + "")));
  }


}
