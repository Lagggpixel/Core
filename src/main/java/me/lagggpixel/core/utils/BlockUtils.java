package me.lagggpixel.core.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockUtils {
  
  public static boolean isBlockTillable(@NotNull Block block) {
    Material material = block.getType();
    return material == Material.DIRT || material == Material.GRASS_BLOCK;
  }
  
}
