/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.internal.util;

import me.lagggpixel.core.libs.containr.Container;
import me.lagggpixel.core.libs.containr.StaticContainer;
import org.bukkit.inventory.Inventory;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public final class Containers {

  public static Container ofInv(Inventory inventory) {
    int width = switch (inventory.getType()) {
      case HOPPER -> 5;
      case CRAFTING, DROPPER, ANVIL -> 3;
      case BEACON -> 1;
      default -> 9;
    };
    return new StaticContainer(width, inventory.getSize() / width);
  }

}
