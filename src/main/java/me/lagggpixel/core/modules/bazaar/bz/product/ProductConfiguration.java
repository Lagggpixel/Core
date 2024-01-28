/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz.product;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
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

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
