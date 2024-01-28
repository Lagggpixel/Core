/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders.submit;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
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
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class SellOrderService implements OrderService {
    @Override
    public InstantSubmitResult submit(InstantBazaarOrder order) {
        Product product = order.getProduct();
        Bazaar bazaar = product.getProductCategory().getCategory().getBazaar();
        Player player = Bukkit.getPlayer(order.getPlayer());

        int playerAmount = bazaar.getProductAmountInInventory(product, player);
        if (playerAmount < order.getRealAmount()) {
            return InstantSubmitResult.NOT_ENOUGH;
        }

        takeItems(player, product.getItem(), order.getRealAmount());
        claim(order);
        return InstantSubmitResult.SUCCESS;
    }

    @Override
    public SubmitResult submit(BazaarOrder order) {
        Product product = order.getProduct();
        Bazaar bazaar = product.getProductCategory().getCategory().getBazaar();
        Player player = Bukkit.getPlayer(order.getPlayer());

        int playerAmount = bazaar.getProductAmountInInventory(product, player);
        if (playerAmount < order.getAmount()) {
            return SubmitResult.NOT_ENOUGH;
        }

        takeItems(player, product.getItem(), order.getAmount());

        return SubmitResult.SUCCESS;
    }

    private void takeItems(Player player, ItemStack item, int amount) {
        Inventory inventory = player.getInventory();

        int remainingAmount = amount;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slotItem = inventory.getItem(i);
            if (slotItem == null || !slotItem.isSimilar(item)) continue;

            int slotAmount = slotItem.getAmount();
            if (slotAmount <= remainingAmount) {
                inventory.setItem(i, null);
                remainingAmount -= slotAmount;
            } else {
                slotItem.setAmount(slotAmount - remainingAmount);
                remainingAmount = 0;
            }

            if (remainingAmount <= 0) {
                player.updateInventory();
                return;
            }
        }
    }

    @Override
    public int claim(BazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        Economy economy = order.getProduct().getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
        double availableCoins = order.getAvailableCoins();

        economy.depositPlayer(player, availableCoins);

        return order.getAvailableItems();
    }

    @Override
    public int claim(InstantBazaarOrder order) {
        Player player = Bukkit.getPlayer(order.getPlayer());
        Economy economy = order.getProduct().getProductCategory().getCategory().getBazaar().getBazaarApi().getEconomy();
        double coins = order.getPrice();
        economy.depositPlayer(player, coins);
        return order.getRealAmount();
    }
}
