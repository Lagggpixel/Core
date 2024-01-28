/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.component.element;

import me.lagggpixel.core.libs.containr.Element;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class EmptyElement extends Element {

    @Contract(" -> new")
    @NotNull
    public static EmptyElement create() {
        return new EmptyElement();
    }

    @Nullable
    @Override
    public ItemStack item(Player player) {
        return null;
    }

}
