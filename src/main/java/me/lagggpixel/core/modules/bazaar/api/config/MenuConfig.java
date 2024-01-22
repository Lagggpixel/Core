/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.config;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface MenuConfig {
    List<String> getStringList(String path, Placeholder... placeholders);

    String getString(String path, MessagePlaceholder... placeholders);

    ItemStack replaceLorePlaceholders(ItemStack icon, String placeholder, Placeholder... innerPlaceholders);
}
