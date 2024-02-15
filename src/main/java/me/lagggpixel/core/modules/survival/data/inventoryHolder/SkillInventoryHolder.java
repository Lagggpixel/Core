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

import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.modules.skills.data.Skill;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Lagggpixel
 * @since February 14, 2024
 */
public class SkillInventoryHolder extends SurvivalCoreInventoryHolder {

  private final List<Integer> skillItems = List.of(9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53);

  protected SkillType skillType;

  private final @NotNull Skill skill;

  private int page;

  protected SkillInventoryHolder(Player player, @NotNull SkillType skillType) {
    super(player,
        ChatUtils.stringToComponentCC("&aSkill: " + skillType.getName()),
        54);
    this.skillType = skillType;
    this.skill = user.getSkills().getSkill(skillType);

    initializeInventoryItems();
  }

  protected SkillInventoryHolder(Player player, @NotNull SkillType skillType, int page) {
    super(player,
        ChatUtils.stringToComponentCC("&aSkill: " + skillType.getName()),
        54);
    this.skillType = skillType;

    this.page = page;
    this.skill = user.getSkills().getSkill(skillType);

    initializeInventoryItems();
  }

  @Override
  public void initializeInventoryItems() {
    int level = skill.getLevel();
    int exp = Math.toIntExact(Math.round(skill.getLevelExp()));
    int currentLevelCounter;

    if (page == 0) {
      if (level > 25) {
        page = 2;
      } else {
        page = 1;
      }
    }

    if (page == 2) {
      currentLevelCounter = 26;
    } else {
      currentLevelCounter = 1;
    }

    for (Integer slot : skillItems) {
      if (currentLevelCounter == level + 1) {
        Long skillExpReq = SkillHandler.getSkillExpPerLevel().get(level + 1);
        double percentage = (double) Math.round((float) exp / skillExpReq * 1000) / 10;
        inventory.setItem(slot, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE)
            .setAmount(currentLevelCounter)
            .setDisplayName("&a" + skillType.getName() + " " + currentLevelCounter)
            .setLore(ChatUtils.stringToComponentCC("&7Exp: &a" + exp + " / " + skillExpReq + " (" + percentage + "%)"))
            .toItemStack());
      } else if (currentLevelCounter > level + 1) {
        inventory.setItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
            .setAmount(currentLevelCounter)
            .setDisplayName("&a" + skillType.getName() + " " + currentLevelCounter)
            .toItemStack());
      } else {
        inventory.setItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
            .setAmount(currentLevelCounter + 1)
            .setDisplayName("&a" + skillType.getName() + " " + currentLevelCounter)
            .setLore(skillType.getSkillDescription())
            .toItemStack());
      }
      currentLevelCounter++;
    }

    inventory.setItem(0,
        new ItemBuilder(skillType.getMaterial())
            .setDisplayName("&a" + skillType.getName() + " " + user.getSkills().getFishing().getLevel())
            .setLore(skillType.getSkillDescription())
            .toItemStack());

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

    switch (tag) {
      case "back":
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        new SkillsInventoryHolder(player).openInventory(player);
        break;
      case "firstPage":
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        new SkillInventoryHolder(player, skillType, 1).openInventory(player);
        break;
      case "secondPage":
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        new SkillInventoryHolder(player, skillType, 2).openInventory(player);
        break;
      case "close":
        break;
    }
  }
}
