package me.lagggpixel.core.modules.merchant;

import lombok.Getter;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.guilds.commands.MerchantCommand;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.handler.MerchantHandler;
import me.lagggpixel.core.modules.merchant.handler.MerchantSellPriceHandler;
import me.lagggpixel.core.utils.CommandUtils;
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
    
    for (Merchant merchant : this.merchantHandler.getMerchants().values()) {
      merchant.createNpc();
    }
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new MerchantCommand(this));
  }
  
  @Override
  public void registerListeners() {
  
  }
}
