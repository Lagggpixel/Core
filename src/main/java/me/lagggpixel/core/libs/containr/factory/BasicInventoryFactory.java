/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.factory;

import lombok.RequiredArgsConstructor;
import me.lagggpixel.core.libs.containr.InventoryInfo;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class BasicInventoryFactory implements InventoryFactory {

    private final String title;
    private final int rows;

    @Override
    public InventoryInfo createInventory(GUI gui) {
        Inventory inventory = Bukkit.createInventory(gui, rows * 9, title);
        return new InventoryInfo(inventory, title);
    }
}
