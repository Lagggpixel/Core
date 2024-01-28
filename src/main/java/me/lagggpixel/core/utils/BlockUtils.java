/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class BlockUtils {
  
  public static boolean isBlockTillable(@NotNull Block block) {
    Material material = block.getType();
    return material == Material.DIRT || material == Material.GRASS_BLOCK;
  }
  
}
