/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.data;

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.CoreInventoryHolder;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public abstract class SurvivalCoreInventoryHolder extends CoreInventoryHolder {
  
  protected SurvivalCoreInventoryHolder(Player player, Component title, int slots) {
    super(player, title, slots);
  }
  
  protected SurvivalCoreInventoryHolder(Player player, String title, int slots) {
    super(player, title, slots);
  }
  
  
  @Override
  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
    event.setCancelled(true);
    if (event.getCurrentItem() == null) {
      return;
    }
    ItemStack clickedItem = event.getCurrentItem();
    String tag = getItemTag(clickedItem);
    if (tag == null) {
      return;
    }
    if (tag.equals("close")) {
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
      player.closeInventory();
    }
  }
  
  protected void buildCloseButton(int slot) {
    this.inventory.setItem(slot, new ItemBuilder((Material.BARRIER))
        .setDisplayName("&cClose")
        .setLore(List.of(
            ChatUtils.stringToComponentCC("&7Click to close")
        ))
        .setTag("close")
        .toItemStack());
  }
  
  protected void buildCloseButton() {
    buildCloseButton(49);
  }
  
  protected void buildBackButton(int slot) {
    this.inventory.setItem(slot, new ItemBuilder(Material.ARROW)
        .setDisplayName("&aGo Back")
        .setLore(List.of(
            ChatUtils.stringToComponentCC("&7Click to go back")
        ))
        .setTag("back")
        .toItemStack());
  }
  
  protected void buildBackButton() {
    buildBackButton(48);
  }
}
