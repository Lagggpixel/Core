/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.data.inventoryHolder;

import lombok.Getter;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Getter
public class SurvivalItemInventoryHolder extends SurvivalCoreInventoryHolder {
  public SurvivalItemInventoryHolder(Player player) {
    super(player, Lang.SURVIVAL_ITEM_INVENTORY_TITLE.toComponent(), 54);
  }

  @Override
  public void initializeInventoryItems() {
    inventory.setItem(19, new ItemBuilder(Material.DIAMOND_SWORD)
        .setDisplayName("&aYour Skills")
        .setLore(List.of(
            ChatUtils.stringToComponentCC("&7View your Skill progression and"),
            ChatUtils.stringToComponentCC("&7rewards."),
            ChatUtils.stringToComponentCC(" "),
            ChatUtils.stringToComponentCC("&eClick to view!")
        ))
        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
        .setTag("skillItem")
        .toItemStack());

    ItemStack comingSoon = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
        .setDisplayName("&cComing Soon!")
        .toItemStack();
    inventory.setItem(20, comingSoon);
    inventory.setItem(21, comingSoon);
    inventory.setItem(22, comingSoon);
    inventory.setItem(23, comingSoon);
    inventory.setItem(24, comingSoon);
    inventory.setItem(25, comingSoon);
    inventory.setItem(29, comingSoon);
    inventory.setItem(30, comingSoon);
    inventory.setItem(31, comingSoon);
    inventory.setItem(32, comingSoon);
    inventory.setItem(33, comingSoon);

    buildCloseButton();

    this.fillEmptySlots();
  }

  @Override
  public void openInventory(@NotNull Player player) {
    player.openInventory(this.getInventory());
  }

  @Override
  public void handleInventoryClick(@NotNull InventoryClickEvent event) {
    super.handleInventoryClick(event);
    if (event.getCurrentItem() == null) {
      return;
    }
    ItemStack clickedItem = event.getCurrentItem();
    String tag = getItemTag(clickedItem);
    if (tag == null) {
      return;
    }
    if (tag.equals("skillItem")) {
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
      new SkillsInventoryHolder(player).openInventory(player);
    }
  }

}
