package me.lagggpixel.core.modules.survival.handlers;

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class SurvivalItemHandler {
  
  private final NamespacedKey nameSpacedKey = new NamespacedKey("core", "survival");
  
  public ItemStack getSurvivalItem() {
    ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR)
        .setDisplayName(Lang.SURVIVAL_ITEM_NAME.toComponent())
        .setLore(ChatUtils.stringToComponentCC("&2Click to open menu."));
    ItemStack itemStack = itemBuilder.toItemStack();
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.getPersistentDataContainer().set(nameSpacedKey, PersistentDataType.STRING, "survival_item");
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
  
  public boolean isSurvivalItem(Item item) {
    if (item == null) {
      return false;
    }
    ItemStack itemStack = item.getItemStack();
    return isSurvivalItem(itemStack);
  }
  
  public boolean isSurvivalItem(ItemStack item) {
    if (item == null) {
      return false;
    }
    if (item.getItemMeta().getPersistentDataContainer().get(nameSpacedKey, PersistentDataType.STRING) == null) {
      return false;
    }
    return Objects.equals(item.getItemMeta().getPersistentDataContainer().get(nameSpacedKey, PersistentDataType.STRING), "survival_item");
  }
  
  public void openInventory(Player player) {
  
  }
}
