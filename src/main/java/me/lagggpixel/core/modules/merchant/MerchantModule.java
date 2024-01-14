package me.lagggpixel.core.modules.merchant;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.merchant.commands.MerchantCommand;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.handler.MerchantHandler;
import me.lagggpixel.core.modules.merchant.handler.MerchantSellPriceHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class MerchantModule implements IModule {
  
  @Getter
  private static MerchantModule instance;
  @Getter
  private MerchantHandler merchantHandler;
  @Getter
  private MerchantSellPriceHandler priceHandler;
  
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
    
    merchantHandler = new MerchantHandler();
    priceHandler = new MerchantSellPriceHandler();
    
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Merchant merchant : merchantHandler.getMerchants().values()) {
          merchant.createNpc();
        }
      }
    }.runTaskLater(Main.getInstance(), 1L);
  }
  
  @Override
  public void onDisable() {
    for (Merchant merchant : this.merchantHandler.getMerchants().values()) {
      merchant.unregister();
    }
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new MerchantCommand(this));
  }
  
  @Override
  public void registerListeners() {
  
  }
}
