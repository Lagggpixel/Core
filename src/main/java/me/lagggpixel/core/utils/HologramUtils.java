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

import me.lagggpixel.core.data.Hologram;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class HologramUtils {
  
  public static Map<String, Hologram> holograms = new HashMap<>();
  
  public static void despawnAll() {
    holograms.forEach((id, hologram) -> hologram.destroy());
  }
}
