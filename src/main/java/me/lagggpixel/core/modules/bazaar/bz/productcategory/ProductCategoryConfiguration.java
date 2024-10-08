/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.productcategory;

import lombok.Getter;
import me.lagggpixel.core.modules.bazaar.bz.product.ProductConfiguration;
import me.lagggpixel.core.modules.bazaar.menu.configurations.ProductCategoryMenuConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since January 22, 2024
 */
@Getter
public class ProductCategoryConfiguration implements ConfigurationSerializable {
    private final ProductCategoryMenuConfiguration menuConfig;
    private final List<ProductConfiguration> products;
    private ItemStack icon;
    private String name;

    public ProductCategoryConfiguration(ProductCategoryMenuConfiguration menuConfig, ItemStack icon, String name, List<ProductConfiguration> products) {
        this.menuConfig = menuConfig;
        this.icon = icon;
        this.name = name;
        this.products = new ArrayList<>(products);
    }

    public static ProductCategoryConfiguration deserialize(Map<String, Object> args) {
        return new ProductCategoryConfiguration((ProductCategoryMenuConfiguration) args.get("menu-config"),
                (ItemStack) args.get("icon"),
                (String) args.get("name"),
                (List<ProductConfiguration>) args.get("products"));
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("menu-config", menuConfig);
        args.put("icon", icon);
        args.put("name", name);
        args.put("products", products);
        return args;
    }
}
