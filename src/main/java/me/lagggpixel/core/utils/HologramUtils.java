package me.lagggpixel.core.utils;

import me.lagggpixel.core.data.Hologram;

import java.util.HashMap;
import java.util.Map;

public class HologramUtils {
  
  public static Map<String, Hologram> holograms = new HashMap<>();
  
  public static void despawnAll() {
    holograms.forEach((id, hologram) -> hologram.destroy());
  }
}
