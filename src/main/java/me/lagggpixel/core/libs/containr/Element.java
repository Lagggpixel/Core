/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr;

import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Getter
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public abstract class Element implements Component {

    private final String id;

    public Element() {
        this.id = RandomStringUtils.randomAlphabetic(8);
    }

    public void click(ContextClickInfo info) {}

    @Nullable
    public abstract ItemStack item(Player player);

}
