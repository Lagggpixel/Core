/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;

public interface CompressedBazaarOrder {
    boolean canAddOrder(BazaarOrder order);

    boolean addOrder(BazaarOrder order);

    Product getProduct();

    int getAmount();

    double getUnitPrice();

    OrderType getType();

    int getOrderAmount();

    BazaarOrder getSampleOrder();
}
