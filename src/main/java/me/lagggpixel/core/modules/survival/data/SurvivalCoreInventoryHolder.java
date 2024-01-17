/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.data;

import me.lagggpixel.core.data.CoreInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class SurvivalCoreInventoryHolder extends CoreInventoryHolder {
  protected SurvivalCoreInventoryHolder(Player player, Component title, int slots) {
    super(player, title, slots);
  }
  
  protected SurvivalCoreInventoryHolder(Player player, String title, int slots) {
    super(player, title, slots);
  }
}
