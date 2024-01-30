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

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @since January 22, 2024
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
