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

import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Lagggpixel
 * @since January 22, 2024
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
        new ItemBuilder(SkillType.FARMING.getMaterial())
            .setDisplayName("&aFarming " + user.getSkills().getFarming().getLevel())
            .setLore(SkillType.FARMING.getSkillDescription())
            .setTag("farming")
            .toItemStack()
    );

    inventory.setItem(21,
        new ItemBuilder(SkillType.MINING.getMaterial())
            .setDisplayName("&aMining " + user.getSkills().getMining().getLevel())
            .setLore(SkillType.MINING.getSkillDescription())
            .setTag("mining")
            .toItemStack()
    );


    inventory.setItem(22,
        new ItemBuilder(SkillType.COMBAT.getMaterial())
            .setDisplayName("&aCombat " + user.getSkills().getCombat().getLevel())
            .setLore(SkillType.COMBAT.getSkillDescription())
            .setTag("combat")
            .toItemStack()
    );


    inventory.setItem(23,
        new ItemBuilder(SkillType.WOODCUTTING.getMaterial())
            .setDisplayName("&aWoodcutting " + user.getSkills().getWoodcutting().getLevel())
            .setLore(SkillType.WOODCUTTING.getSkillDescription())
            .setTag("woodcutting")
            .toItemStack()
    );

    inventory.setItem(24,
        new ItemBuilder(SkillType.FISHING.getMaterial())
            .setDisplayName("&aFishing " + user.getSkills().getFishing().getLevel())
            .setLore(SkillType.FISHING.getSkillDescription())
            .setTag("fishing")
            .toItemStack()
    );


    this.buildBackButton();
    this.buildCloseButton();

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
    SkillType skill;
    switch (tag) {
      case "close":
        break;
      case "back":
        new SurvivalItemInventoryHolder(player).openInventory(player);
        break;
      case "farming":
        skill = SkillType.FARMING;
      case "mining":
        skill = SkillType.MINING;
      case "combat":
        skill = SkillType.COMBAT;
      case "fishing":
        skill = SkillType.FISHING;
      case "woodcutting":
        skill = SkillType.WOODCUTTING;
        new SkillInventoryHolder(player, skill).openInventory(player);
        break;
    }
  }
}
