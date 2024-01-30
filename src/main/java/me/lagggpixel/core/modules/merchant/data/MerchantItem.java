/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.merchant.data;

import lombok.Data;
import org.bukkit.Material;

/**
 * @author Exortions
 * @since January 22, 2024
 */
@Data
public class MerchantItem {
  private final Material material;
  private final int cost;
  private final int slot;

  public int getRawSlot() {
    return slot;
  }

  public int getSlot() {
    if (slot <= 7) {
      return slot + 9;
    } else if (slot <= 14) {
      return slot + 11;
    } else if (slot <= 21) {
      return slot + 13;
    } else if (slot <= 28) {
      return slot + 15;
    }
    throw new IllegalArgumentException("[Merchant Item] Invalid slot: " + slot);
  }
}