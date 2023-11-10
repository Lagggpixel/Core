package me.lagggpixel.core.modules.economy.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class EconomyCommand extends CommandClass {
  
  private final EconomyModule economyModule;
  private final EconomyManager economyManager;
  
  public EconomyCommand(EconomyModule economyModule, EconomyManager economyManager) {
    this.economyModule = economyModule;
    this.economyManager = economyManager;
  }
  
  @Override
  public String getCommandName() {
    return "economy";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("economy", "eco");
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
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    if (args.length != 3) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    String subcommand = args[0].toLowerCase();
    String playerName = args[1];
    double amount;
    
    try {
      amount = Double.parseDouble(args[2]);
    } catch (NumberFormatException e) {
      sender.sendMessage(Lang.ECONOMY_INVALID_AMOUNT.toComponentWithPrefix());
      return true;
    }
    
    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
    User user = Main.getUser(offlinePlayer.getUniqueId());
    
    switch (subcommand) {
      case "give":
        economyManager.deposit(offlinePlayer, amount);
        sender.sendMessage(Lang.ECONOMY_GIVE.toComponentWithPrefix(Map.of("%player%", user.getPlayerName(), "%amount%", String.valueOf(amount))));
        break;
      case "set":
        economyManager.setBalance(offlinePlayer, amount);
        sender.sendMessage(Lang.ECONOMY_SET.toComponentWithPrefix(Map.of("%player%", user.getPlayerName())));
        break;
      case "remove":
        economyManager.withdraw(offlinePlayer, amount);
        sender.sendMessage(Lang.ECONOMY_REMOVE.toComponentWithPrefix(Map.of("%player%", user.getPlayerName(), "%amount%", String.valueOf(amount))) );
        break;
      default:
        sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        break;
    }
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
