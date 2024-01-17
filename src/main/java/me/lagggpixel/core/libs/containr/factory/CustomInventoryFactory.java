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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class CustomInventoryFactory implements InventoryFactory {

    private final InventoryType type;
    private final String title;

    @Override
    public InventoryInfo createInventory(GUI gui) {
        Inventory inventory = Bukkit.createInventory(gui, type, title);
        return new InventoryInfo(inventory, title);
    }
}
