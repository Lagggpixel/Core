/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar;

import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @since January 22, 2024
 */
public interface Category extends MenuInfo {
  ItemStack getIcon();

  void setIcon(ItemStack icon);

  String getName();

  void setName(String name);

  List<ProductCategory> getProductCategories();

  void addProductCategory(ProductCategory productCategory);

  void removeProductCategory(ProductCategory productCategory);

  GUI getMenu();

  void setTitle(String name);

  GUI getEditMenu();

  Bazaar getBazaar();
}
