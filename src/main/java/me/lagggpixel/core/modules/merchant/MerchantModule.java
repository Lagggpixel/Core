/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.merchant;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.merchant.commands.MerchantCommand;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.handler.MerchantHandler;
import me.lagggpixel.core.modules.merchant.handler.MerchantSellPriceHandler;
import me.lagggpixel.core.utils.CommandUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.mineskin.MineskinClient;

public class MerchantModule implements IModule {
  
  @Getter
  private static MerchantModule instance;
  @Getter
  private MerchantHandler merchantHandler;
  @Getter
  private MerchantSellPriceHandler priceHandler;
  @Getter
  private MineskinClient mineskinClient;
  
  private final String key = "2d2e77095f985e96176f98292bb8c625f63c7457fb370dcf1e2e62b8e2f0edb0";
  private final String editingKey = "eafd9a8923f9247970ae6a501091aa352a1163a79786b862b7d9aa2cbaa50c45197dcebd1495ddacf30250a5a587cef1a57209d34558e8570212abfe98215e95";
  
  
  @NotNull
  @Override
  public String getId() {
    return "merchant";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
    instance = this;
    
    mineskinClient = new MineskinClient("InfiniteMinecrafters", key);
    
    merchantHandler = new MerchantHandler();
    priceHandler = new MerchantSellPriceHandler();
    
    new BukkitRunnable() {
      @Override
      public void run() {
        Main.getInstance().getLogger().info("Loading merchants...");
        long timeStarted = System.currentTimeMillis();
        for (Merchant merchant : merchantHandler.getMerchants().values()) {
          merchant.createNpc();
        }
        CitizensAPI.getNPCRegistry().saveToStore();
        long timeTaken = System.currentTimeMillis() - timeStarted;
        Main.getInstance().getLogger().info("Loaded " + merchantHandler.getMerchants().size() + " merchants in " + timeTaken + "ms");
      }
    }.runTaskLater(Main.getInstance(), 20L*5);
  }
  
  @Override
  public void onDisable() {
    long timeStarted = System.currentTimeMillis();
    for (Merchant merchant : this.merchantHandler.getMerchants().values()) {
      merchant.unregister();
    }
    CitizensAPI.getNPCRegistry().saveToStore();
    long timeTaken = System.currentTimeMillis() - timeStarted;
    Main.getInstance().getLogger().info("Unloaded " + this.merchantHandler.getMerchants().size() + " merchants in " + timeTaken + "ms");
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new MerchantCommand(this));
  }
  
  @Override
  public void registerListeners() {
  
  }
}
