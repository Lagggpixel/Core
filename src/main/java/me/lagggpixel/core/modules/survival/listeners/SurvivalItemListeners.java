package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.modules.survival.handlers.SurvivalItemHandler;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SurvivalItemListeners implements Listener {
  
  private final SurvivalItemHandler survivalItemHandler;
  
  public SurvivalItemListeners(SurvivalItemHandler survivalItemHandler) {
    this.survivalItemHandler = survivalItemHandler;
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void PlayerDropItemEvent(@NotNull PlayerDropItemEvent event) {
    Item itemDropped = event.getItemDrop();
    if (survivalItemHandler.isSurvivalItem(itemDropped)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void InventoryClickEvent(@NotNull InventoryClickEvent event) {
    InventoryAction inventoryAction = event.getAction();
    ItemStack currentItem = event.getClick() ==
        ClickType.NUMBER_KEY
        ? event.getWhoClicked().getInventory().getItem(event.getHotbarButton())
        : event.getCurrentItem();
    if (survivalItemHandler.isSurvivalItem(currentItem)) {
      event.setCancelled(true);
      if (inventoryAction != InventoryAction.HOTBAR_SWAP) {
        survivalItemHandler.openInventory((Player) event.getWhoClicked());
      }
    }
  }
  
  private void handleSurvivalItemClickEvent(InventoryClickEvent event) {
  }
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void PlayerSwapHandItemsEvent(@NotNull PlayerSwapHandItemsEvent event) {
    ItemStack itemSwapped = event.getOffHandItem();
    if (survivalItemHandler.isSurvivalItem(itemSwapped)) {
      event.setCancelled(true);
    }
  }
  
}
