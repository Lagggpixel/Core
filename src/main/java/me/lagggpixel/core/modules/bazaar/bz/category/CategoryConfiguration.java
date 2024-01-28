/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz.category;

import me.lagggpixel.core.modules.bazaar.bz.productcategory.ProductCategoryConfiguration;
import me.lagggpixel.core.modules.bazaar.menu.configurations.CategoryMenuConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class CategoryConfiguration implements ConfigurationSerializable {
    private final CategoryMenuConfiguration menuConfig;
    private final List<ProductCategoryConfiguration> productCategories;
    private ItemStack icon;
    private String name;

    public CategoryConfiguration(CategoryMenuConfiguration menuConfig,
                                 ItemStack icon,
                                 String name,
                                 List<ProductCategoryConfiguration> productCategories) {
        this.menuConfig = menuConfig;
        this.icon = icon;
        this.name = name;
        this.productCategories = new ArrayList<>(productCategories);
    }

    public static CategoryConfiguration deserialize(Map<String, Object> args) {
        return new CategoryConfiguration((CategoryMenuConfiguration) args.get("menu-config"),
                (ItemStack) args.get("icon"),
                (String) args.get("name"),
                (List<ProductCategoryConfiguration>) args.get("product-categories"));
    }

    public CategoryMenuConfiguration getMenuConfig() {
        return menuConfig;
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

    public List<ProductCategoryConfiguration> getProductCategories() {
        return productCategories;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("menu-config", menuConfig);
        args.put("icon", icon);
        args.put("name", name);
        args.put("product-categories", productCategories);
        return args;
    }
}
