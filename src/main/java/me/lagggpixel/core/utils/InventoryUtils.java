package me.lagggpixel.core.utils;

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.modules.merchant.data.Gui;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {
  
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
  
  public static void fillEmpty(Gui gui) {
    for (int i = 0; i < gui.getSlots(); i++)
      gui.addItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack());
  }
  
  public static void fillEmpty(Inventory inventory, Material material) {
    for (int i = 0; i < inventory.getSize(); i++)
      inventory.setItem(i, new ItemBuilder(material).setDisplayName(" ").toItemStack());
  }
}
