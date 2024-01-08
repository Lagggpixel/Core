package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.survival.data.survivalItem.SurvivalItemInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {
  
  public InventoryClickListener() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void InventoryClickEvent(InventoryClickEvent event) {
    Inventory inventory = event.getInventory();
    if (inventory.getHolder(false) instanceof SurvivalItemInventoryHolder inventoryHolder) {
      event.setCancelled(true);
      inventoryHolder.handleInventoryClick(event);
    }
  }
  
}
