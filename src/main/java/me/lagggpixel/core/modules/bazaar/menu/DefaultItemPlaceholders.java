/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;
import me.lagggpixel.core.modules.bazaar.api.config.MenuConfig;
import me.lagggpixel.core.modules.bazaar.api.config.MessagePlaceholder;
import me.lagggpixel.core.modules.bazaar.api.menu.ItemPlaceholderFunction;
import me.lagggpixel.core.modules.bazaar.api.menu.ItemPlaceholders;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.modules.bazaar.utils.MenuUtils;
import me.lagggpixel.core.modules.bazaar.utils.Utils;
import me.lagggpixel.core.libs.containr.ContainerComponent;
import me.lagggpixel.core.libs.containr.internal.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class DefaultItemPlaceholders implements ItemPlaceholders {
  public static final int ORDER_LIMIT = 5;
  private final BazaarModule module;
  private final List<ItemPlaceholderFunction> itemPlaceholders = new ArrayList<>();

  public DefaultItemPlaceholders(BazaarModule module) {
    this.module = module;

    MenuConfig menuConfig = module.getMenuConfig();

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> replaceMultiLinePlaceholder(menuConfig,
        containerComponent,
        item,
        itemSlot,
        player,
        "sell-inventory",
        CompletableFuture.supplyAsync(() -> {
          Map<Product, Integer> productsInInventory = module.getBazaar().getProductsInInventory(player);
          if (productsInInventory.isEmpty()) return new ArrayList<>();

          List<String> lore = menuConfig.getStringList("sell-inventory");

          double totalEarnedCoins = 0;

          int itemsLineIndex = getPlaceholderLoreLineIndex(lore, "items");
          lore.remove(itemsLineIndex);

          int currentItemIndex = 0;
          for (Map.Entry<Product, Integer> productAmountEntry : productsInInventory.entrySet()) {
            Product product = productAmountEntry.getKey();
            int playerAmount = productAmountEntry.getValue();

            Pair<Double, Integer> sellPriceWithOrderableAmount = product.getSellPriceWithOrderableAmount(playerAmount).join();
            double totalItemPrice = sellPriceWithOrderableAmount.getKey();
            int amount = sellPriceWithOrderableAmount.getValue();

            List<String> itemAsLore = menuConfig.getStringList("item",
                new MessagePlaceholder("item-amount", String.valueOf(amount)),
                new MessagePlaceholder("item-name", product.getName()),
                new MessagePlaceholder("item-coins", Utils.getTextPrice(totalItemPrice)));

            for (int j = 0; j < itemAsLore.size(); j++) {
              String loreLine = itemAsLore.get(j);
              lore.add(itemsLineIndex + currentItemIndex + j, loreLine);
            }

            currentItemIndex += itemAsLore.size();

            totalEarnedCoins += totalItemPrice;
          }

          for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = Utils.colorize(line.replaceAll("%earned-coins%", Utils.getTextPrice(totalEarnedCoins)));
            lore.set(i, line);
          }

          return lore;
        }),
        "loading",
        "sell-inventory-none"));

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
      if (info instanceof Product) {
        Product product = (Product) info;
        return menuConfig.replaceLorePlaceholders(item, "product", new MessagePlaceholder("product", product.getName()));
      }
      if (info instanceof BazaarOrder) {
        BazaarOrder order = (BazaarOrder) info;
        return menuConfig.replaceLorePlaceholders(item, "product", new MessagePlaceholder("product", order.getProduct().getName()));
      }
      if (info instanceof InstantBazaarOrder) {
        InstantBazaarOrder order = (InstantBazaarOrder) info;
        return menuConfig.replaceLorePlaceholders(item, "product", new MessagePlaceholder("product", order.getProduct().getName()));
      }
      return item;

    });

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
      if (!(info instanceof Product)) return item;
      Product product = (Product) info;
      return menuConfig.replaceLorePlaceholders(item,
          "buy-instantly",
          new LazyLorePlaceholder(module,
              containerComponent,
              item,
              itemSlot,
              player,
              "buy-price",
              product.getLowestBuyPrice().thenApply(Utils::getTextPrice),
              menuConfig.getString("loading")),
          new LazyLorePlaceholder(module,
              containerComponent,
              item,
              itemSlot,
              player,
              "stack-buy-price",
              product.getLowestBuyPrice().thenApply(price -> Utils.getTextPrice(price * 64)),
              menuConfig.getString("loading")));
    });

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
      if (!(info instanceof Product)) return item;
      Product product = (Product) info;
      int productAmountInInventory = module.getBazaar().getProductAmountInInventory(product, player);
      return menuConfig.replaceLorePlaceholders(item,
          "sell-instantly",
          new MessagePlaceholder("item-amount", String.valueOf(productAmountInInventory)),
          new LazyLorePlaceholder(module,
              containerComponent,
              item,
              itemSlot,
              player,
              "coins",
              module.getOrderManager().prepareInstantOrder(product, productAmountInInventory, OrderType.SELL, player.getUniqueId()).thenApply(instantOrder -> Utils.getTextPrice(instantOrder.getPrice())),
              menuConfig.getString("loading")));
    });

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
      if (!(info instanceof Product)) return item;
      Product product = (Product) info;

      return replaceMultiLinePlaceholder(menuConfig,
          containerComponent,
          item,
          itemSlot,
          player,
          "buy-orders",
          module.getOrderManager().getCompressedOrders(product, OrderType.BUY, ORDER_LIMIT)
              .thenApply(orders -> orders.stream()
                  .flatMap(order -> menuConfig.getStringList("buy-order",
                      new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice())),
                      new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                      new MessagePlaceholder("orders", String.valueOf(order.getOrderAmount()))).stream())
                  .collect(Collectors.toList())),
          "loading",
          "buy-order-none");
    });

    addItemPlaceholder((containerComponent, item, itemSlot, player, info) -> {
      if (!(info instanceof Product)) return item;
      Product product = (Product) info;

      return replaceMultiLinePlaceholder(menuConfig,
          containerComponent,
          item,
          itemSlot,
          player,
          "sell-offers",
          module.getOrderManager().getCompressedOrders(product, OrderType.SELL, ORDER_LIMIT)
              .thenApply(orders -> orders.stream()
                  .flatMap(order -> menuConfig.getStringList("sell-offer",
                      new MessagePlaceholder("coins", Utils.getTextPrice(order.getUnitPrice())),
                      new MessagePlaceholder("amount", String.valueOf(order.getAmount())),
                      new MessagePlaceholder("offers", String.valueOf(order.getOrderAmount()))).stream())
                  .collect(Collectors.toList())),
          "loading",
          "sell-offer-none");
    });
  }

  @Override
  public void addItemPlaceholder(ItemPlaceholderFunction action) {
    itemPlaceholders.add(action);
  }

  @Override
  public ItemStack replaceItemPlaceholders(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info) {
    ItemStack newItem = item.clone();
    for (ItemPlaceholderFunction itemPlaceholder : itemPlaceholders) {
      newItem = itemPlaceholder.apply(containerComponent, newItem, itemSlot, player, info);
    }
    return newItem;
  }

  private int getPlaceholderLoreLineIndex(List<String> lore, String placeholder) {
    int sellInventoryLineIndex = -1;
    for (int i = 0; i < lore.size(); i++) {
      String line = lore.get(i);
      if (!line.equals("%" + placeholder + "%")) continue;
      sellInventoryLineIndex = i;
      break;
    }
    return sellInventoryLineIndex;
  }

  private ItemStack replaceMultiLinePlaceholder(MenuConfig menuConfig,
                                                ContainerComponent container,
                                                ItemStack item,
                                                int itemSlot,
                                                Player player,
                                                String placeholder,
                                                CompletableFuture<List<String>> linesFuture,
                                                String loadingPlaceholder,
                                                String nonePlaceholder) {
    ItemStack newItem = item.clone();
    ItemMeta itemMeta = newItem.getItemMeta();

    if (itemMeta == null || !itemMeta.hasLore()) return item;

    List<String> lore = new ArrayList<>(itemMeta.getLore());

    int placeholderLineIndex = getPlaceholderLoreLineIndex(lore, placeholder);
    if (placeholderLineIndex == -1) return item;

    lore.remove(placeholderLineIndex);

    List<String> loadingPlaceholderValue = menuConfig.getStringList(loadingPlaceholder);
    addLinesToLore(loadingPlaceholderValue, lore, placeholderLineIndex);

    itemMeta.setLore(lore);
    newItem.setItemMeta(itemMeta);

    linesFuture.thenAccept(lines -> {
      for (int i = 0; i < loadingPlaceholderValue.size(); i++) {
        lore.remove(placeholderLineIndex + i);
      }

      if (lines.isEmpty()) {
        List<String> nonePlaceholderValue = menuConfig.getStringList(nonePlaceholder);
        addLinesToLore(nonePlaceholderValue, lore, placeholderLineIndex);
      } else {
        addLinesToLore(lines, lore, placeholderLineIndex);
      }

      itemMeta.setLore(lore);
      newItem.setItemMeta(itemMeta);

      MenuUtils.updateGuiItem(module.getMenuHistory(), container, itemSlot, player, newItem);
    });

    return newItem;
  }

  private void addLinesToLore(List<String> placeholderValue, List<String> lore, int index) {
    for (int i = 0; i < placeholderValue.size(); i++) {
      String placeholderLine = placeholderValue.get(i);
      lore.add(index + i, placeholderLine);
    }
  }
}
