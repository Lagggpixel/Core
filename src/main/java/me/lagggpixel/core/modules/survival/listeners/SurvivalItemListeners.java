package me.lagggpixel.core.modules.survival.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.survival.handlers.SurvivalItemHandler;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class SurvivalItemListeners implements Listener {
  
  private final SurvivalItemHandler survivalItemHandler;
  
  public SurvivalItemListeners(SurvivalItemHandler survivalItemHandler) {
    this.survivalItemHandler = survivalItemHandler;
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    new BukkitRunnable() {
      @Override
      public void run() {
        player.getInventory().setItem(8, survivalItemHandler.getSurvivalItem());
      }
    }.runTaskLater(Main.getInstance(), 20);
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onWorldChange(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    new BukkitRunnable() {
      @Override
      public void run() {
        player.getInventory().setItem(8, survivalItemHandler.getSurvivalItem());
      }
    }.runTaskLater(Main.getInstance(), 20);
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
  
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void PlayerSwapHandItemsEvent(@NotNull PlayerSwapHandItemsEvent event) {
    ItemStack itemSwapped = event.getOffHandItem();
    if (survivalItemHandler.isSurvivalItem(itemSwapped)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerClick(@NotNull PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (survivalItemHandler.isSurvivalItem(event.getPlayer().getInventory().getItemInMainHand())) {
      event.setCancelled(true);
      survivalItemHandler.openInventory(player);
    }
  }
  
  
  
}
