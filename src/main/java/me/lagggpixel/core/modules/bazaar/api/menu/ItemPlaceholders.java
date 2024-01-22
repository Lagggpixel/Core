/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.libs.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface ItemPlaceholders {
    void addItemPlaceholder(ItemPlaceholderFunction action);

    ItemStack replaceItemPlaceholders(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info);
}
