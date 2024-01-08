package me.lagggpixel.core.modules.survival.data.survivalItem;

import me.lagggpixel.core.modules.survival.data.SurvivalCoreInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class SkillsInventoryHolder extends SurvivalCoreInventoryHolder {
  protected SkillsInventoryHolder(Player player, Component title, int slots) {
    super(player, title, slots);
  }
  
  @Override
  public void initializeInventoryItems() {
  
  }
  
  @Override
  public void openInventory(@NotNull Player player) {
  
  }
  
  @Override
  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
  
  }
}
