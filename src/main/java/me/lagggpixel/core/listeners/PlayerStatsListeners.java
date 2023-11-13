package me.lagggpixel.core.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerStatsListeners implements Listener {
  
  public PlayerStatsListeners() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockBreak(@NotNull BlockBreakEvent event) {
    User user = Main.getUser(event.getPlayer().getUniqueId());
    if (user == null) {
      return;
    }
    Material material = event.getBlock().getType();
    
    if (user.getBlocksBroken().containsKey(material)) {
      user.getBlocksBroken().replace(material, user.getBlocksBroken().get(material) + 1);
    }
    else {
      user.getBlocksBroken().put(material, 1L);
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockPlace(@NotNull BlockPlaceEvent event) {
    User user = Main.getUser(event.getPlayer().getUniqueId());
    if (user == null) {
      return;
    }
    Material material = event.getBlock().getType();
    
    if (user.getBlocksPlaced().containsKey(material)) {
      user.getBlocksPlaced().replace(material, user.getBlocksPlaced().get(material) + 1);
    }
    else {
      user.getBlocksPlaced().put(material, 1L);
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onItemCraft(@NotNull CraftItemEvent event) {
    User user = Main.getUser(event.getWhoClicked().getUniqueId());
    if (user == null) {
      return;
    }
    Material material = event.getRecipe().getResult().getType();
    int amount = event.getRecipe().getResult().getAmount();
    
    if (user.getItemsCrafted().containsKey(material)) {
      user.getItemsCrafted().replace(material, user.getItemsCrafted().get(material) + amount);
    }
    else {
      user.getItemsCrafted().put(material, 1L);
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEntityKill(@NotNull EntityDamageByEntityEvent event) {
    
    if (!(event.getDamager() instanceof Player player) || !event.getEntity().isDead()) {
      return;
    }
    
    User user = Main.getUser(player.getUniqueId());
    EntityType entityType = event.getEntity().getType();
    
    if (user.getEntityKills().containsKey(entityType)) {
      user.getEntityKills().replace(entityType, user.getEntityKills().get(entityType) + 1);
    }
    else {
      user.getEntityKills().put(entityType, 1L);
    }
  }
  
}
