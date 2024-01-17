/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.survival.data.SurvivalCoreInventoryHolder;
import me.lagggpixel.core.modules.survival.data.survivalItem.SurvivalItemInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryClickListener implements Listener {
  
  public InventoryClickListener() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void InventoryClickEvent(@NotNull InventoryClickEvent event) {
    Inventory inventory = event.getInventory();
    if (inventory.getHolder(false) instanceof SurvivalCoreInventoryHolder survivalCoreInventoryHolder) {
      event.setCancelled(true);
      if (survivalCoreInventoryHolder instanceof SurvivalItemInventoryHolder inventoryHolder) {
        inventoryHolder.handleInventoryClick(event);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryMoveItem(InventoryMoveItemEvent event) {
    Inventory inventory = event.getSource();
    if (inventory.getHolder(false) instanceof SurvivalCoreInventoryHolder) {
      event.setCancelled(true);
    }
  }
}
