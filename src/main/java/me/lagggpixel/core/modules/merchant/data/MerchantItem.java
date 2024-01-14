package me.lagggpixel.core.modules.merchant.data;

import lombok.Data;
import org.bukkit.Material;

@Data
public class MerchantItem {
  private final Material material;
  private final int cost;
  private final int slot;
  
  public int getSlot() {
    if (slot <= 7) {
      return slot + 9;
    } else if (slot <= 14) {
      return slot + 18;
    } else if (slot <= 21) {
      return slot + 27;
    } else if (slot <= 28) {
      return slot + 36;
    }
    throw new IllegalArgumentException("[Merchant Item] Invalid slot: " + slot);
  }
}