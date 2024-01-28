/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.economy;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.economy.commands.BalanceCommand;
import me.lagggpixel.core.modules.economy.commands.BalanceTopCommand;
import me.lagggpixel.core.modules.economy.commands.EconomyCommand;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class EconomyModule implements IModule {
  
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
