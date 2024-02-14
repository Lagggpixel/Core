/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skills.enums;

import lombok.Getter;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;

import java.util.List;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Getter
public enum SkillType {

  FARMING(Material.GOLDEN_HOE, List.of(
      ChatUtils.stringToComponentCC("&7Harvest crops and shear sheep to"),
      ChatUtils.stringToComponentCC("&7earn Farming XP."),
      ChatUtils.stringToComponentCC(" "),
      ChatUtils.stringToComponentCC("&eClick to view!")
  )),
  MINING(Material.STONE_PICKAXE, List.of(
      ChatUtils.stringToTextComponentCC("&7Dive into deep caves and find"),
      ChatUtils.stringToTextComponentCC("&7rare ores and valuable materials"),
      ChatUtils.stringToTextComponentCC("&7to earn mining XP!"),
      ChatUtils.stringToTextComponentCC(" "),
      ChatUtils.stringToTextComponentCC("&eClick to view!")
  )),
  COMBAT(Material.STONE_SWORD, List.of(
      ChatUtils.stringToTextComponentCC("&7Fight mobs and special bosses to"),
      ChatUtils.stringToTextComponentCC("&7earn Combat XP!"),
      ChatUtils.stringToTextComponentCC(" "),
      ChatUtils.stringToTextComponentCC("&eClick to view!")
  )),
  FISHING(Material.FISHING_ROD, List.of(
      ChatUtils.stringToTextComponentCC("&7Visit your local pond to fish"),
      ChatUtils.stringToTextComponentCC("&7and earn fishing XP!"),
      ChatUtils.stringToTextComponentCC(" "),
      ChatUtils.stringToTextComponentCC("&eClick to view!"))),
  WOODCUTTING(Material.JUNGLE_SAPLING, List.of(
      ChatUtils.stringToTextComponentCC("&7Cut trees and forage for other"),
      ChatUtils.stringToTextComponentCC("&7plants to earn Foraging XP!"),
      ChatUtils.stringToTextComponentCC(" "),
      ChatUtils.stringToTextComponentCC("&eClick to view!")
  ));

  private final Material material;
  private final List<Component> skillDescription;

  SkillType(Material material, List<Component> skillDescription) {
    this.material = material;
    this.skillDescription = skillDescription;
  }

  public String getName() {
    return StringUtils.capitalize(name().toLowerCase());
  }

}
