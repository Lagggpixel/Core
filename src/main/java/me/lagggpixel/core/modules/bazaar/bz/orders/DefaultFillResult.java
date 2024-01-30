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

import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.FillResult;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderManager;

import java.util.List;

/**
 * @since January 22, 2024
 */
public class DefaultFillResult implements FillResult {
  private final OrderManager orderManager;
  private final List<BazaarOrder> previousOrders;
  private final int amount;
  private final double price;

  public DefaultFillResult(OrderManager orderManager, List<BazaarOrder> previousOrders, int amount, double price) {
    this.orderManager = orderManager;
    this.previousOrders = previousOrders;
    this.amount = amount;
    this.price = price;
  }

  @Override
  public int getAmount() {
    return amount;
  }

  @Override
  public double getPrice() {
    return price;
  }

  @Override
  public void undoFill() {
    for (BazaarOrder previousOrder : previousOrders) {
      orderManager.undoOrderFill(previousOrder);
    }
  }
}
