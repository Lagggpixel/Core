/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar;

import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
