package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.modules.survival.handlers.SurvivalItemHandler;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class SurvivalItemListeners implements Listener {
  
  private final SurvivalItemHandler survivalItemHandler;
  
  public SurvivalItemListeners(SurvivalItemHandler survivalItemHandler) {
    this.survivalItemHandler = survivalItemHandler;
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void PlayerDropItemEvent(PlayerDropItemEvent event) {
    Item itemDropped = event.getItemDrop();
    if (survivalItemHandler.isSurvivalItem(itemDropped)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void InventoryClickEvent(InventoryClickEvent event) {
    ItemStack itemClicked = event.getCurrentItem();
    if (survivalItemHandler.isSurvivalItem(itemClicked)) {
      event.setCancelled(true);
      survivalItemHandler.openInventory((Player) event.getWhoClicked());
    }
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void PlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
    ItemStack itemSwapped = event.getOffHandItem();
    if (survivalItemHandler.isSurvivalItem(itemSwapped)) {
      event.setCancelled(true);
    }
  }
  
}
