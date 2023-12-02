package me.lagggpixel.core.modules.economy;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.economy.commands.BalanceCommand;
import me.lagggpixel.core.modules.economy.commands.BalanceTopCommand;
import me.lagggpixel.core.modules.economy.commands.EconomyCommand;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;

public class EconomyModule extends Module {
  
  EconomyImpl economyImpl;
  EconomyManager economyManager;
  
  
  @NotNull
  @Override
  public String getId() {
    return "economy";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
    this.economyManager = new EconomyManager();
    this.economyImpl = new EconomyImpl(this, economyManager);
    
    ServicesManager services = Main.getInstance().getServer().getServicesManager();
    services.register(Economy.class, this.economyImpl, Main.getInstance(), ServicePriority.High);
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new BalanceCommand(this, economyManager));
    CommandUtils.registerCommand(new EconomyCommand(this, economyManager));
    CommandUtils.registerCommand(new BalanceTopCommand(this, economyManager));
  }
  
  @Override
  public void registerListeners() {
  
  }
}
