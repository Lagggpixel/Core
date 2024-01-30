/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.category;

import lombok.Getter;
import me.lagggpixel.core.modules.bazaar.bz.productcategory.ProductCategoryConfiguration;
import me.lagggpixel.core.modules.bazaar.menu.configurations.CategoryMenuConfiguration;
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
    args.put("product-categories", productCategories);
    return args;
  }
}
