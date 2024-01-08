package me.lagggpixel.core.modules.survival.data.survivalItem;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class SurvivalItemInventoryHolder implements InventoryHolder {
  private final Inventory inventory;
  
  /**
   * The player object of who opened this inventory
   */
  private final Player player;
  /**
   * The user object of who opened this inventory
   */
  private final User user;
  
  private final Component title;
  private final ItemStack blankItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack();
  
  public SurvivalItemInventoryHolder(Player player) {
    this.player = player;
    this.user = Main.getUser(player);
    
    this.title = Lang.SURVIVAL_ITEM_INVENTORY_TITLE.toComponent();
    this.inventory = Main.getInstance().getServer().createInventory(this, 9, this.title);
    this.initializeInventoryItems();
  }
  
  private void initializeInventoryItems() {
    
    
    this.fillEmptySlots();
  }
  
  private void fillEmptySlots() {
    for (int i = 0; i < inventory.getContents().length; i++) {
      ItemStack itemStack = inventory.getContents()[i];
      if (itemStack == null) {
        inventory.setItem(i, blankItem);
      }
    }
  }
  
  @Override
  public @NotNull Inventory getInventory() {
    return this.inventory;
  }
  
}
