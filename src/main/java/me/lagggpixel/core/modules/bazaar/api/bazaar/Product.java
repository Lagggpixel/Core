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
import me.lagggpixel.core.libs.containr.ContainerComponent;
import me.lagggpixel.core.libs.containr.internal.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * @since January 22, 2024
 */
public interface Product extends MenuInfo {
  ItemStack getItem();

  void setItem(ItemStack item);

  ItemStack getIcon(ContainerComponent container, int itemSlot, Player player);

  ItemStack getRawIcon();

  void setIcon(ItemStack icon);

  ItemStack getConfirmationIcon(double unitPrice, int amount);

  String getName();

  void setName(String name);

  String getId();

  ProductCategory getProductCategory();

  CompletableFuture<Double> getLowestBuyPrice();

  CompletableFuture<Double> getHighestSellPrice();

  CompletableFuture<Pair<Double, Integer>> getBuyPriceWithOrderableAmount(int amount);

  CompletableFuture<Pair<Double, Integer>> getSellPriceWithOrderableAmount(int amount);
}
