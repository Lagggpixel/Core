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

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface ProductCategory extends MenuInfo {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    ItemStack getRawIcon();

    String getName();

    void setName(String name);

    List<Product> getProducts();

    void addProduct(Product product);

    void removeProduct(Product product);

    GUI getMenu();

    GUI getEditMenu();

    Category getCategory();
}
