/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.product;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @since January 22, 2024
 */
@Setter
@Getter
public class ProductConfiguration implements ConfigurationSerializable {
    private ItemStack item;
    private ItemStack icon;
    private String name;

    public ProductConfiguration(ItemStack item, ItemStack icon, String name) {
        this.item = item;
        this.icon = icon;
        this.name = name;
    }

    public static ProductConfiguration deserialize(Map<String, Object> args) {
        return new ProductConfiguration((ItemStack) args.get("item"),
                (ItemStack) args.get("icon"),
                (String) args.get("name"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("item", item);
        args.put("icon", icon);
        args.put("name", name);
        return args;
    }
}
