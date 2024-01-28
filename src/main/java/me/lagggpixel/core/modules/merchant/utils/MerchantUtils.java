/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.merchant.utils;

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.modules.merchant.data.MerchantGui;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class MerchantUtils {
  
  public static void fillBorder(Inventory inventory) {
    for (int i = 0; i < 9; i++)
      inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
    for (int i = 45; i < 54; i++)
      inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
    for (int i = 9; i < 45; i += 9)
      inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
    for (int i = 17; i < 45; i += 9)
      inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
  }
  
  public static void fillEmpty(Inventory inventory) {
    fillEmpty(inventory, Material.GRAY_STAINED_GLASS_PANE);
  }
  
  public static void fillEmpty(MerchantGui merchantGui) {
    for (int i = 0; i < merchantGui.getSlots(); i++)
      merchantGui.addItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
  }
  
  public static void fillEmpty(Inventory inventory, Material material) {
    for (int i = 0; i < inventory.getSize(); i++)
      inventory.setItem(i, new ItemBuilder(material).setDisplayName(" ").toItemStack());
  }
}
