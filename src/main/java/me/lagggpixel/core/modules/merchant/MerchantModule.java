package me.lagggpixel.core.modules.merchant;

import lombok.Getter;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.merchant.handler.MerchantHandler;
import me.lagggpixel.core.modules.merchant.handler.PriceHandler;
import org.jetbrains.annotations.NotNull;

public class MerchantModule implements IModule {
  
  @Getter
  private static MerchantModule instance;
  @Getter
  private MerchantHandler merchantHandler;
  @Getter
  private PriceHandler priceHandler;
  
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
    priceHandler = new PriceHandler();
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
  
  }
}
