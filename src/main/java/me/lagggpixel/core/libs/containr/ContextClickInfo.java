/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public final class ContextClickInfo {

    private final GUI gui;
    private final Container container;
    private final Element element;
    private final Player player;
    private final ClickType clickType;
    private final ItemStack cursor;

    public void close() {
        gui.close(player);
    }

}
