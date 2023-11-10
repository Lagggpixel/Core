package me.lagggpixel.core.modules.economy.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BalanceCommand extends CommandClass {
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
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    if (args.length > 1) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    Player targetPlayer;
    if (args.length == 1) {
      // TODO - Add permission
      if (!sender.hasPermission("placeholder_permission")) {
        sender.sendMessage(Main.getInstance().getServer().permissionMessage());
        return true;
      }
      
      targetPlayer = Bukkit.getPlayer(args[0]);
      if (targetPlayer == null) {
        sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", args[0])));
        return true;
      }
    } else {
      if (!(sender instanceof Player)) {
        sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        return true;
      }
      
      targetPlayer = (Player) sender;
    }
    
    double balance = economyManager.getBalance(targetPlayer);
    
    if (targetPlayer.equals(sender)) {
      sender.sendMessage(Lang.ECONOMY_BALANCE_SELF.toComponentWithPrefix(Map.of("%balance%", String.valueOf(balance))));
      return true;
    }
    else{
      sender.sendMessage(Lang.ECONOMY_BALANCE_OTHER.toComponentWithPrefix(Map.of("%player%", targetPlayer.getName(), "%balance%", String.valueOf(balance))));
    }
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
