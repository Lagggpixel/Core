/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Category;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.ProductCategory;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.bz.category.CategoryImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class BazaarImpl implements Bazaar {
  private final BazaarModule module;
  private final List<Category> categories;

  public BazaarImpl(BazaarModule module) {
    this.module = module;
    this.categories = module.getBazaarConfig().getCategories().stream()
        .map(categoryConfiguration -> new CategoryImpl(this, categoryConfiguration))
        .collect(Collectors.toList());
  }

  @Override
  public void open(Player player) {
    open(player, categories.get(0));
  }

  @Override
  public void open(Player player, Category category) {
    category.getMenu().open(player);
  }

  @Override
  public void openEdit(Player player, Category category) {
    category.getEditMenu().open(player);
  }

  @Override
  public void openSearch(Player player, String filter) {
    module.getBazaarConfig().getSearchMenuConfiguration().getMenu(this, filter, false).open(player);
  }

  @Override
  public void openEditSearch(Player player, String filter, ConfigurableMenuItem searchItem) {
    module.getBazaarConfig().getSearchMenuConfiguration().getMenu(this, "", true).open(player);
  }

  @Override
  public void openOrders(Player player) {
    module.getBazaarConfig().getOrdersMenuConfiguration().getMenu(getBazaarApi(), false).open(player);
  }

  @Override
  public void openEditOrders(Player player) {
    module.getBazaarConfig().getOrdersMenuConfiguration().getMenu(getBazaarApi(), true).open(player);
  }

  @Override
  public void openProduct(Player player, Product product) {
    module.getBazaarConfig().getProductMenuConfiguration().getMenu(product, false).open(player);
  }

  @Override
  public void openProductEdit(Player player, Product product) {
    module.getBazaarConfig().getProductMenuConfiguration().getMenu(product, true).open(player);
  }

  @Override
  public List<Category> getCategories() {
    return categories;
  }

  @Override
  public void saveConfig() {
    module.getBazaarConfig().save();
  }

  @Override
  public BazaarAPI getBazaarApi() {
    return module;
  }

  @Override
  public Product getProduct(String id) {
    return getProducts(product -> product.getId().equals(id)).stream().findAny().orElse(null);
  }

  @Override
  public List<Product> getProducts() {
    return getProducts(product -> true);
  }

  @Override
  public List<Product> getProducts(Predicate<Product> filter) {
    List<Product> products = new ArrayList<>();

    for (Category category : categories) {
      for (ProductCategory productCategory : category.getProductCategories()) {
        for (Product product : productCategory.getProducts()) {
          if (!filter.test(product)) continue;
          products.add(product);
        }
      }
    }

    return products;
  }

  @Override
  public Map<Product, Integer> getProductsInInventory(Player player) {
    Map<Product, Integer> productsInInventory = new HashMap<>();

    for (Product product : getProducts()) {
      int totalAmount = getProductAmountInInventory(product, player);
      if (totalAmount > 0) {
        productsInInventory.put(product, totalAmount);
      }
    }

    return productsInInventory;
  }

  @Override
  public int getProductAmountInInventory(Product product, Player player) {
    int amount = 0;

    for (ItemStack itemStack : player.getInventory().getContents()) {
      if (itemStack == null) continue;

      if (itemStack.isSimilar(product.getItem())) {
        amount += itemStack.getAmount();
      }
    }

    return amount;
  }
}
