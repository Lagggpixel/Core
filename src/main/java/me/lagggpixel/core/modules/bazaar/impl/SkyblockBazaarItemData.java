package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItemData;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarOffer;

import java.util.List;

@Data
public class SkyblockBazaarItemData implements BazaarItemData {
  
  private final double productAmount;
  private final double buyPrice;
  private final double sellPrice;
  private final BazaarItemVolume buyVolume;
  private final int last7dInstantBuyVolume;
  private final BazaarItemVolume sellVolume;
  private final int last7dInstantSellVolume;
  private final List<BazaarOffer> orders;
  private final List<BazaarOffer> offers;
  
  @Data
  public static class SkyblockBazaarItemVolume implements BazaarItemVolume {
    private final double amount;
    private final double volume;
  }
  
}
