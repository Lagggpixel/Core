/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.data.survivalItem;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.survival.data.SurvivalCoreInventoryHolder;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class SkillsInventoryHolder extends SurvivalCoreInventoryHolder {
  protected SkillsInventoryHolder(Player player) {
    super(player, "Your Skills", 54);
  }
  
  @Override
  public void initializeInventoryItems() {
    User user = Main.getUser(player);
    
    inventory.setItem(13,
        new ItemBuilder(Material.DIAMOND_SWORD)
            .setDisplayName("&aYour Skills")
            .setLore(List.of(
                ChatUtils.stringToComponentCC("&7View your Skill progression"),
                ChatUtils.stringToComponentCC("&7and rewards.")
            ))
            .toItemStack()
    );
    
    inventory.setItem(20,
        new ItemBuilder(Material.GOLDEN_HOE)
            .setDisplayName("&aFarming " + user.getSkills().getFarming().getLevel())
            .setLore(List.of(
                ChatUtils.stringToComponentCC("&7Harvest crops and shear sheep to"),
                ChatUtils.stringToComponentCC("&7earn Farming XP."),
                ChatUtils.stringToComponentCC(" "),
                ChatUtils.stringToComponentCC("&eClick to view!")
            ))
            .toItemStack()
    );
    
    inventory.setItem(21,
        new ItemBuilder(Material.STONE_PICKAXE)
            .setDisplayName("&aMining " + user.getSkills().getMining().getLevel())
            .setLore(List.of(
                ChatUtils.stringToTextComponentCC("&7Dive into deep caves and find"),
                ChatUtils.stringToTextComponentCC("&7rare ores and valuable materials"),
                ChatUtils.stringToTextComponentCC("&7to earn mining XP!"),
                ChatUtils.stringToTextComponentCC(" "),
                ChatUtils.stringToTextComponentCC("&eClick to view!")
            ))
            .toItemStack()
    );
    
    
    inventory.setItem(22,
        new ItemBuilder(Material.STONE_SWORD)
            .setDisplayName("&aCombat " + user.getSkills().getCombat().getLevel())
            .setLore(List.of(
                ChatUtils.stringToTextComponentCC("&7Fight mobs and special bosses to"),
                ChatUtils.stringToTextComponentCC("&7earn Combat XP!"),
                ChatUtils.stringToTextComponentCC(" "),
                ChatUtils.stringToTextComponentCC("&eClick to view!")))
            .toItemStack()
    );
    
    
    inventory.setItem(23,
        new ItemBuilder(Material.JUNGLE_SAPLING)
            .setDisplayName("&aWoodcutting " + user.getSkills().getWoodcutting().getLevel())
            .setLore(List.of(
                ChatUtils.stringToTextComponentCC("&7Cut trees and forage for other"),
                ChatUtils.stringToTextComponentCC("&7plants to earn Foraging XP!"),
                ChatUtils.stringToTextComponentCC(" "),
                ChatUtils.stringToTextComponentCC("&eClick to view!")
            ))
            .toItemStack()
    );
    
    inventory.setItem(24,
        new ItemBuilder(Material.FISHING_ROD)
            .setDisplayName("&aFishing " + user.getSkills().getFishing().getLevel())
            .setLore(List.of(
                ChatUtils.stringToTextComponentCC("&7Visit your local pond to fish"),
                ChatUtils.stringToTextComponentCC("&7and earn fishing XP!"),
                ChatUtils.stringToTextComponentCC(" "),
                ChatUtils.stringToTextComponentCC("&eClick to view!")))
            .toItemStack()
    );
    
    
    buildBackButton();
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
    if (tag.equalsIgnoreCase("back")) {
      new SurvivalItemInventoryHolder(player).openInventory(player);
      return;
    }
  }
}
