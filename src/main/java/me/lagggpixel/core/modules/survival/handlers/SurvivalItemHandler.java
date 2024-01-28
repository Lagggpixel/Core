/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.survival.data.survivalItem.SurvivalItemInventoryHolder;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class SurvivalItemHandler {
  
  private final NamespacedKey nameSpacedKey = new NamespacedKey(Main.getInstance(), "survival");
  
  public ItemStack getSurvivalItem() {
    ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR)
        .setDisplayName(Lang.SURVIVAL_ITEM_NAME.toComponent())
        .setLore(ChatUtils.stringToComponentCC("&2Click to open menu."));
    ItemStack itemStack = itemBuilder.toItemStack();
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.getPersistentDataContainer().set(nameSpacedKey, PersistentDataType.STRING, "survival_item");
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
  
  public boolean isSurvivalItem(Item item) {
    if (item == null) {
      return false;
    }
    ItemStack itemStack = item.getItemStack();
    return isSurvivalItem(itemStack);
  }
  
  public boolean isSurvivalItem(ItemStack item) {
    if (item == null) {
      return false;
    }
    if (item.getItemMeta() == null) {
      return false;
    }
    if (!item.getItemMeta().getPersistentDataContainer().has(nameSpacedKey, PersistentDataType.STRING)) {
      return false;
    }
    return (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(nameSpacedKey, PersistentDataType.STRING)).equals("survival_item"));
  }
  
  public void openInventory(Player player) {
    new SurvivalItemInventoryHolder(player).openInventory(player);
  }
}
