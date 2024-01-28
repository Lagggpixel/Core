/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.CompressedBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;

import java.util.HashSet;
import java.util.Set;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
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
