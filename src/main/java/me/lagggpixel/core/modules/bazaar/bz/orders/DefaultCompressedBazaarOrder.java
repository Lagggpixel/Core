/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.CompressedBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;

import java.util.HashSet;
import java.util.Set;

/**
 * @since January 22, 2024
 */
public class DefaultCompressedBazaarOrder implements CompressedBazaarOrder {
  private final Set<BazaarOrder> orders;

  public DefaultCompressedBazaarOrder(Set<BazaarOrder> orders) {
    this.orders = orders;
  }

  public DefaultCompressedBazaarOrder(BazaarOrder initialOrder) {
    this(new HashSet<>());
    this.orders.add(initialOrder);
  }

  @Override
  public boolean canAddOrder(BazaarOrder order) {
    return order.isSimilar(getSampleOrder());
  }

  @Override
  public boolean addOrder(BazaarOrder order) {
    if (!canAddOrder(order)) return false;
    orders.add(order);
    return true;
  }

  @Override
  public Product getProduct() {
    return getSampleOrder().getProduct();
  }

  @Override
  public int getAmount() {
    return orders.stream().mapToInt(BazaarOrder::getOrderableItems).sum();
  }

  @Override
  public double getUnitPrice() {
    return getSampleOrder().getUnitPrice();
  }

  @Override
  public OrderType getType() {
    return getSampleOrder().getType();
  }

  @Override
  public int getOrderAmount() {
    return orders.size();
  }

  @Override
  public BazaarOrder getSampleOrder() {
    return orders.iterator().next();
  }
}
