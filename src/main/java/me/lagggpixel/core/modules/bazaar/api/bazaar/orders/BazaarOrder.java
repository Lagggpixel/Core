/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.UUID;

/**
 * @since January 22, 2024
 */
public interface BazaarOrder extends MenuInfo {
  Product getProduct();

  int getAmount();

  double getUnitPrice();

  OrderType getType();

  UUID getPlayer();

  int getFilled();

  Instant getCreatedAt();

  void fill(int amount);

  int getClaimed();

  void claim(int amount);

  boolean isSimilar(BazaarOrder other);

  ItemStack getIcon();

  double getAvailableCoins();

  int getOrderableItems();

  int getAvailableItems();

  int getDatabaseId();
}
