package me.lagggpixel.core.modules.economy;

import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("DeprecatedIsStillUsed")
public class EconomyImpl extends AbstractEconomy {
  
  EconomyModule module;
  EconomyManager economyManager;
  
  public EconomyImpl(EconomyModule module, EconomyManager economyManager) {
    this.module = module;
    this.economyManager = economyManager;
  }
  
  @Override
  public boolean isEnabled() {
    return module.isEnabled();
  }
  
  @Override
  public String getName() {
    return "SurvivalCore_Economy";
  }
  
  @Override
  public String format(double amount) {
    return "%name%%amount%";
  }
  
  @Override
  public int fractionalDigits() {
    return -1;
  }
  
  @Override
  public String currencyNamePlural() {
    return "$";
  }
  
  @Override
  public String currencyNameSingular() {
    return "$";
  }
  
  @Override
  public boolean createPlayerAccount(String playerName) {
    this.hasAccount(playerName);
    return false;
  }
  
  @Override
  public boolean createPlayerAccount(String playerName, String worldName) {
    return this.createPlayerAccount(playerName);
  }
  
  @Override
  public double getBalance(OfflinePlayer player, String world) {
    return this.getBalance(player);
  }
  
  @Override
  public double getBalance(OfflinePlayer player) {
    return this.economyManager.getBalance(player.getUniqueId());
  }
  
  @Override
  @Deprecated
  public double getBalance(String playerName, String world) {
    return this.getBalance(playerName);
  }
  
  @Override
  @Deprecated
  public double getBalance(String playerName) {
    return economyManager.getBalance(playerName);
  }
  
  @Override
  public boolean hasAccount(OfflinePlayer player, String worldName) {
    return this.hasAccount(player);
  }
  
  @Override
  public boolean hasAccount(OfflinePlayer player) {
    return this.economyManager.hasAccount(player.getUniqueId());
  }
  
  @Override
  @Deprecated
  public boolean hasAccount(String playerName, String worldName) {
    return this.hasAccount(playerName);
  }
  
  @Override
  @Deprecated
  public boolean hasAccount(String playerName) {
    return economyManager.hasAccount(playerName);
  }
  
  @Override
  public boolean has(OfflinePlayer player, String worldName, double amount) {
    return this.has(player, amount);
  }
  
  @Override
  public boolean has(OfflinePlayer player, double amount) {
    return this.economyManager.hasEnough(player.getUniqueId(), amount);
  }
  
  @Override
  @Deprecated
  public boolean has(String playerName, String worldName, double amount) {
    return this.has(playerName, amount);
  }
  
  @Override
  @Deprecated
  public boolean has(String playerName, double amount) {
    return economyManager.hasEnough(playerName, amount);
  }
  
  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
    return this.depositPlayer(player, amount);
  }
  
  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
    amount = Math.abs(amount);
    EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
    String error = null;
    
    double balance;
    if (this.economyManager.deposit(player, amount)) {
      balance = this.economyManager.getBalance(player);
      type = EconomyResponse.ResponseType.SUCCESS;
    } else {
      balance = this.economyManager.getBalance(player);
      error = "Could not deposit " + amount + " to " + player.getUniqueId() + " because something went wrong.";
    }
    return new EconomyResponse(amount, balance, type, error);
  }
  
  @Override
  @Deprecated
  public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
    return this.depositPlayer(playerName, amount);
  }
  
  @Override
  @Deprecated
  public EconomyResponse depositPlayer(String playerName, double amount) {
    if (amount < 0D) {
      return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative amount.");
    }
    EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
    String error = null;
    
    double balance;
    if (economyManager.deposit(playerName, amount)) {
      balance = economyManager.getBalance(playerName);
      type = EconomyResponse.ResponseType.SUCCESS;
    } else {
      balance = economyManager.getBalance(playerName);
      error = "Could not deposit " + amount + " to " + playerName + " because something went wrong.";
    }
    return new EconomyResponse(amount, balance, type, error);
  }
  
  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
    return this.withdrawPlayer(player, amount);
  }
  
  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
    if (amount < 0D) {
      return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.FAILURE, "Can't withdraw negative amount.");
    }
    EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
    String error = null;
    
    double balance;
    if (this.economyManager.withdraw(player, amount)) {
      balance = this.economyManager.getBalance(player);
      type = EconomyResponse.ResponseType.SUCCESS;
    } else {
      balance = this.economyManager.getBalance(player);
      error = "Could not withdraw " + amount + " from " + player.getUniqueId() + " because something went wrong.";
    }
    return new EconomyResponse(amount, balance, type, error);
  }
  
  @Override
  @Deprecated
  public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
    return this.withdrawPlayer(playerName, amount);
  }
  
  @Override
  @Deprecated
  public EconomyResponse withdrawPlayer(String playerName, double amount) {
    if (amount < 0D) {
      return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.FAILURE, "Can't withdraw negative amounts.");
    }
    EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
    String error = null;
    
    double balance;
    if (economyManager.withdraw(playerName, amount)) {
      balance = economyManager.getBalance(playerName);
      type = EconomyResponse.ResponseType.SUCCESS;
    } else {
      balance = economyManager.getBalance(playerName);
      error = "Could not withdraw " + amount + " from " + playerName + " because something went wrong.";
    }
    return new EconomyResponse(amount, balance, type, error);
  }
  
  @Override
  public EconomyResponse createBank(String name, String player) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse deleteBank(String name) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse bankHas(String name, double amount) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse bankWithdraw(String name, double amount) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse bankDeposit(String name, double amount) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse isBankOwner(String name, String playerName) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse isBankMember(String name, String playerName) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public EconomyResponse bankBalance(String name) {
    return new EconomyResponse(0D, 0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SurvivalCore Economy does not support bank accounts!");
  }
  
  @Override
  public List<String> getBanks() {
    return new ArrayList<>();
  }
  
  @Override
  public boolean hasBankSupport() {
    return false;
  }
}
