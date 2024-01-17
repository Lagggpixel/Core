/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.component.element;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lagggpixel.core.libs.containr.Element;
import me.lagggpixel.core.libs.containr.internal.util.CyclicArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public abstract class AnimatedElement extends Element {

    private final CyclicArrayList<Supplier<ItemStack>> parts;
    private boolean paused;

    public AnimatedElement() {
        this(Lists.newArrayList());
        this.paused = false;
    }

    public AnimatedElement(List<ItemStack> parts) {
        CyclicArrayList<Supplier<ItemStack>> suppliers = new CyclicArrayList<>();
        for (ItemStack part : parts) {
            suppliers.add(() -> part);
        }
        this.parts = suppliers;
    }

    public AnimatedElement paused(boolean paused) {
        this.paused = paused;
        return this;
    }

    @Nullable
    @Override
    public ItemStack item(Player player) {
        Optional<Supplier<ItemStack>> itemOptional = paused ? parts.getCurrent() : parts.getNext();
        return itemOptional.map(Supplier::get).orElse(null);
    }

}
