package me.lagggpixel.core.modules.economy;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
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
  public void initialize() {
    this.economyManager = new EconomyManager();
    this.economyImpl = new EconomyImpl(this, economyManager);
    
    ServicesManager services = Main.getInstance().getServer().getServicesManager();
    services.register(Economy.class, this.economyImpl, Main.getInstance(), ServicePriority.High);
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
  
  }
}
