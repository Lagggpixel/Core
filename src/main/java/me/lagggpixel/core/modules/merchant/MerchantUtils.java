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

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.modules.merchant.data.MerchantGui;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

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
