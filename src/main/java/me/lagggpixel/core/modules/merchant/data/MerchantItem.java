package me.lagggpixel.core.modules.merchant.data;

import lombok.Data;
import org.bukkit.Material;

@Data
public class MerchantItem {
  private final Material material;
  private final int cost;
}