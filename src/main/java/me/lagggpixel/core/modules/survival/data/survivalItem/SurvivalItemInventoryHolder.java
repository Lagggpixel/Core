package me.lagggpixel.core.modules.survival.data.survivalItem;

import lombok.Getter;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.CoreInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * This class represents the survival item inventory holder.
 * @see SkillsInventoryHolder
 */
@Getter
public class SurvivalItemInventoryHolder extends CoreInventoryHolder {
  public SurvivalItemInventoryHolder(Player player) {
    super(player, Lang.SURVIVAL_ITEM_INVENTORY_TITLE.toComponent(), 54);
    this.initializeInventoryItems();
  }
  
  @Override
  public void initializeInventoryItems() {
    this.fillEmptySlots();
  }
  
  @Override
  public void openInventory(Player player) {
    player.openInventory(this.getInventory());
  }
  
  @Override
  public void handleInventoryClick(InventoryClickEvent event) {
  
  }
  
}
