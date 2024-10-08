/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders.submit;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantSubmitResult;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.SubmitResult;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @since January 22, 2024
 */
public class BuyOrderService implements OrderService {

  @Override
  public InstantSubmitResult submit(InstantBazaarOrder order) {
    Product product = order.getProduct();
    Economy economy = product.getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
    Player player = Bukkit.getPlayer(order.getPlayer());
    double price = order.getRealPrice();

    if (!economy.has(player, price)) {
      return InstantSubmitResult.NOT_ENOUGH;
    }

    economy.withdrawPlayer(player, price);
    claim(order);
    return InstantSubmitResult.SUCCESS;
  }

  @Override
  public SubmitResult submit(BazaarOrder order) {
    Product product = order.getProduct();
    Economy economy = product.getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
    Player player = Bukkit.getPlayer(order.getPlayer());
    double totalPrice = order.getUnitPrice() * order.getAmount();

    if (!economy.has(player, totalPrice)) {
      return SubmitResult.NOT_ENOUGH;
    }

    economy.withdrawPlayer(player, totalPrice);
    return SubmitResult.SUCCESS;
  }

  @Override
  public int claim(BazaarOrder order) {
    return claim(Bukkit.getPlayer(order.getPlayer()), order.getProduct().getItem(), order.getAvailableItems());
  }

  @Override
  public int claim(InstantBazaarOrder order) {
    return claim(Bukkit.getPlayer(order.getPlayer()), order.getProduct().getItem(), order.getRealAmount());
  }

  private int claim(Player player, ItemStack item, int amount) {
    Inventory inventory = player.getInventory();

    int remainingAmount = amount;

    if (remainingAmount == 0) return 0;

    for (int i = 0; i < inventory.getSize(); i++) {
      ItemStack slotItem = inventory.getItem(i);
      if (slotItem != null && !slotItem.isSimilar(item)) continue;

      int slotAvailableAmount = item.getMaxStackSize() - (slotItem == null ? 0 : slotItem.getAmount());
      if (slotAvailableAmount <= 0) continue;

      if (slotItem == null) {
        inventory.setItem(i, item);
      }

      slotItem = inventory.getItem(i);

      if (slotAvailableAmount >= remainingAmount) {
        slotItem.setAmount(slotAvailableAmount == item.getMaxStackSize() ? remainingAmount : slotItem.getAmount() + remainingAmount);
        remainingAmount = 0;
      } else {
        slotItem.setAmount(item.getMaxStackSize());
        remainingAmount -= slotAvailableAmount;
      }

      if (remainingAmount == 0) {
        player.updateInventory();
        break;
      }
    }

    return amount - remainingAmount;
  }
}
