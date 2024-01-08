package me.lagggpixel.core.data;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class CoreInventoryHolder implements InventoryHolder {
  
  public final Inventory inventory;
  public final Component title;
  
  /**
   * The player object of who opened this inventory
   */
  public final Player player;
  /**
   * The user object of who opened this inventory
   */
  public final User user;
  
  protected final ItemStack blankItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").toItemStack();
  
  protected CoreInventoryHolder(Player player, Component title, int slots) {
    this.player = player;
    this.user = Main.getUser(player);
    
    this.title = title;
    this.inventory = Main.getInstance().getServer().createInventory(this, slots, this.title);
  }
  
  protected CoreInventoryHolder(Player player, String title, int slots) {
    this.player = player;
    this.user = Main.getUser(player);
    
    this.title = ChatUtils.stringToComponentCC(title);
    this.inventory = Main.getInstance().getServer().createInventory(this, slots, this.title);
  }
  
  public abstract void initializeInventoryItems();
  
  public void fillEmptySlots() {
    for (int i = 0; i < inventory.getContents().length; i++) {
      ItemStack itemStack = inventory.getContents()[i];
      if (itemStack == null) {
        inventory.setItem(i, blankItem);
      }
    }
  }
  
  public abstract void openInventory(@NotNull Player player);
  
  public abstract void handleInventoryClick(@NotNull InventoryClickEvent event);
}
