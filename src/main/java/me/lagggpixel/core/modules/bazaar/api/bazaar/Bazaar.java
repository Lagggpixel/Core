/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar;

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface Bazaar {
  void open(Player player);

  void open(Player player, Category category);

  void openEdit(Player player, Category category);

  void openSearch(Player player, String filter);

  void openEditSearch(Player player, String filter, ConfigurableMenuItem searchItem);

  void openOrders(Player player);

  void openEditOrders(Player player);

  void openProduct(Player player, Product product);

  void openProductEdit(Player player, Product product);

  List<Category> getCategories();

  void saveConfig();

  BazaarAPI getBazaarApi();

  Product getProduct(String id);

  List<Product> getProducts();

  List<Product> getProducts(Predicate<Product> filter);

  Map<Product, Integer> getProductsInInventory(Player player);

  int getProductAmountInInventory(Product product, Player player);
}
