/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.edit;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Category;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.ProductCategory;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import me.lagggpixel.core.libs.containr.ContextClickInfo;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @since January 22, 2024
 */
public interface EditManager {
  void openItemEdit(Player player, ConfigurableMenuItem configurableMenuItem);

  void openCategoryEdit(Player player, Category category);

  void openProductCategoryEdit(Player player, ProductCategory productCategory);

  void openProductEdit(Player player, Product product);

  Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction, Consumer<ContextClickInfo> defaultEditClickAction, Consumer<ContextClickInfo> editClickAction, Consumer<ContextClickInfo> removeClickAction, Consumer<ContextClickInfo> updateMenu, boolean editing);

  Consumer<ContextClickInfo> createEditableItemClickAction(Consumer<ContextClickInfo> defaultClickAction, Consumer<ContextClickInfo> defaultEditClickAction, Consumer<ContextClickInfo> editClickAction, boolean editing);
}
