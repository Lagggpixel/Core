package me.lagggpixel.core.modules.survival.data.survivalItem;

import lombok.Getter;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.survival.data.SurvivalCoreInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the survival item inventory holder.
 * @see SkillsInventoryHolder
 */
@Getter
public class SurvivalItemInventoryHolder extends SurvivalCoreInventoryHolder {
  public SurvivalItemInventoryHolder(Player player) {
    super(player, Lang.SURVIVAL_ITEM_INVENTORY_TITLE.toComponent(), 54);
    this.initializeInventoryItems();
  }
  
  @Override
  public void initializeInventoryItems() {
    this.fillEmptySlots();
  }
  
  @Override
  public void openInventory(@NotNull Player player) {
    player.openInventory(this.getInventory());
  }
  
  @Override
  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
  
  }
  
}
