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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * @since January 22, 2024
 */
public interface OrderManager {
  BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player);

  CompletableFuture<InstantBazaarOrder> prepareInstantOrder(Product product, int amount, OrderType type, UUID player);

  int claimOrder(BazaarOrder order);

  CompletableFuture<SubmitResult> submitBazaarOrder(BazaarOrder order);

  CompletableFuture<InstantSubmitResult> submitInstantOrder(InstantBazaarOrder order);

  CompletableFuture<List<BazaarOrder>> getOrders(Product product, OrderType type, Predicate<List<BazaarOrder>> shouldContinuePredicate);

  CompletableFuture<List<CompressedBazaarOrder>> getCompressedOrders(Product product, OrderType orderType, int limit);

  CompletableFuture<List<BazaarOrder>> getUnclaimedOrders(UUID playerUniqueId);

  CompletableFuture<FillResult> fillOrders(Product product, OrderType orderType, int amount);

  CompletableFuture<Void> undoOrderFill(BazaarOrder previousOrder);
}
