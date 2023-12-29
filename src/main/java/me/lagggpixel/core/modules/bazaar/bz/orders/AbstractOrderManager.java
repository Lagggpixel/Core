package me.lagggpixel.core.modules.bazaar.bz.orders;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.*;
import me.lagggpixel.core.modules.bazaar.bz.orders.submit.BuyOrderService;
import me.lagggpixel.core.modules.bazaar.bz.orders.submit.OrderService;
import me.lagggpixel.core.modules.bazaar.bz.orders.submit.SellOrderService;
import me.lagggpixel.core.modules.bazaar.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractOrderManager implements OrderManager {
  protected final BazaarModule module;

  protected AbstractOrderManager(BazaarModule module) {
    this.module = module;
  }

  protected static OrderService getOrderService(OrderType type) {
    return type == OrderType.BUY ? new BuyOrderService() : new SellOrderService();
  }

  @Override
  public BazaarOrder prepareBazaarOrder(Product product, int amount, double unitPrice, OrderType type, UUID player) {
    return new DefaultBazaarOrder(product, amount, BigDecimal.valueOf(unitPrice).setScale(1, RoundingMode.DOWN).doubleValue(), type, player, 0, 0, Instant.now());
  }

  @Override
  public CompletableFuture<InstantBazaarOrder> prepareInstantOrder(Product product, int amount, OrderType type, UUID player) {
    AtomicInteger amountInOrders = new AtomicInteger();
    return getOrders(product, type == OrderType.BUY ? OrderType.SELL : OrderType.BUY, orders -> {
      amountInOrders.addAndGet(orders.get(orders.size() - 1).getOrderableItems());
      return amountInOrders.get() < amount;
    }).thenApply(orders -> new DefaultInstantBazaarOrder(product, Math.min(amountInOrders.get(), amount), type, player, Utils.getTotalPrice(orders, amount)));
  }

  @Override
  public int claimOrder(BazaarOrder order) {
    Player player = Bukkit.getPlayer(order.getPlayer());
    OrderType type = order.getType();
    OrderService orderService = getOrderService(type);

    int claimed = orderService.claim(order);

    if (claimed > 0) {

      if (player != null) {
        if (type == OrderType.BUY) {
          player.sendMessage(Lang.BAZAAR_CLAIM_BUY.toComponentWithPrefix(Map.of(
              "%amount%", String.valueOf(claimed),
              "%total-coins%", Utils.getTextPrice(claimed * order.getUnitPrice()),
              "%unit-coins%", Utils.getTextPrice(order.getUnitPrice()),
              "product", order.getProduct().getName()
          )));
        }
        if (type == OrderType.SELL) {
          player.sendMessage(Lang.BAZAAR_CLAIM_SELL.toComponentWithPrefix(Map.of(
              "%amount%", String.valueOf(claimed),
              "%total-coins%", Utils.getTextPrice(claimed * order.getUnitPrice()),
              "%unit-coins%", Utils.getTextPrice(order.getUnitPrice()),
              "%product%", order.getProduct().getName()
          )));
        }
      }
      registerClaim(order, claimed);
    }

    return claimed;
  }

  @Override
  public CompletableFuture<SubmitResult> submitBazaarOrder(BazaarOrder order) {
    Player player = Bukkit.getPlayer(order.getPlayer());
    OrderType type = order.getType();
    OrderService orderService = getOrderService(type);

    SubmitResult result = orderService.submit(order);
    if (player != null) {
      Lang lang = null;
      if (type == OrderType.BUY) {
        if (result == SubmitResult.NOT_ENOUGH) {
          lang = Lang.BAZAAR_ORDER_BUY_NOT_ENOUGH;
        } else if (result == SubmitResult.SUCCESS) {
          lang = Lang.BAZAAR_ORDER_BUY_SUCCESS;
        }
      }
      if (type == OrderType.SELL) {
        if (result == SubmitResult.NOT_ENOUGH) {
          lang = Lang.BAZAAR_ORDER_SELL_NOT_ENOUGH;
        } else if (result == SubmitResult.SUCCESS) {
          lang = Lang.BAZAAR_ORDER_SELL_SUCCESS;
        }
      }
      if (lang != null) {
        player.sendMessage(lang.toComponentWithPrefix(Map.of(
            "%amount%", String.valueOf(order.getAmount()),
            "%product%", order.getProduct().getName(),
            "%coins%", Utils.getTextPrice(order.getUnitPrice() * order.getAmount())
        )));
      }
    }

    if (result != SubmitResult.SUCCESS) return CompletableFuture.supplyAsync(() -> result);
    return registerBazaarOrder(order);
  }

  @Override
  public CompletableFuture<InstantSubmitResult> submitInstantOrder(InstantBazaarOrder order) {
    Player player = Bukkit.getPlayer(order.getPlayer());
    OrderType type = order.getType();
    OrderService orderService = getOrderService(type);

    return fillOrders(order.getProduct(), type == OrderType.BUY ? OrderType.SELL : OrderType.BUY, order.getAmount()).thenApplyAsync(fillResult -> {
      int amount = fillResult.getAmount();
      double price = fillResult.getPrice();

      if (amount < order.getAmount()) {
        if (player != null) {
          Lang lang = null;
          if (type == OrderType.BUY) {
            lang = Lang.BAZAAR_ORDER_BUY_NOT_ENOUGH;
          }
          if (type == OrderType.SELL) {
            lang = Lang.BAZAAR_ORDER_SELL_NOT_ENOUGH;
          }
          if (lang != null) {
            player.sendMessage(lang.toComponentWithPrefix(Map.of()));
          }
        }
        fillResult.undoFill();
        return InstantSubmitResult.NOT_ENOUGH_STOCK;
      }

      CompletableFuture<InstantSubmitResult> resultCompletableFuture = new CompletableFuture<>();

      order.setRealAmount(amount);
      order.setRealPrice(price);
      Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
        InstantSubmitResult result = orderService.submit(order);
        if (player != null) {
          Lang lang = null;
          if (type == OrderType.BUY) {
            if (result == InstantSubmitResult.NOT_ENOUGH) {
              lang = Lang.BAZAAR_ORDER_BUY_NOT_ENOUGH;
            } else if (result == InstantSubmitResult.SUCCESS) {
              lang = Lang.BAZAAR_ORDER_BUY_SUCCESS;
            }
          }
          if (type == OrderType.SELL) {
            if (result == InstantSubmitResult.NOT_ENOUGH) {
              lang = Lang.BAZAAR_ORDER_SELL_NOT_ENOUGH;
            } else if (result == InstantSubmitResult.SUCCESS) {
              lang = Lang.BAZAAR_ORDER_SELL_SUCCESS;
            }
          }
          if (lang != null) {
            player.sendMessage(lang.toComponentWithPrefix(Map.of(
                "%amount%", String.valueOf(order.getAmount()),
                "%product%", order.getProduct().getName(),
                "%coins%", Utils.getTextPrice(order.getRealPrice()))
            ));
          }
        }
        if (result != InstantSubmitResult.SUCCESS) {
          fillResult.undoFill();
        }
        resultCompletableFuture.complete(result);
      });

      return resultCompletableFuture.join();
    });
  }

  protected abstract CompletableFuture<SubmitResult> registerBazaarOrder(BazaarOrder order);

  protected abstract CompletableFuture<Void> registerClaim(BazaarOrder order, int claimed);

  @Override
  public CompletableFuture<List<CompressedBazaarOrder>> getCompressedOrders(Product product, OrderType orderType, int limit) {
    return CompletableFuture.supplyAsync(() -> {
      List<CompressedBazaarOrder> compressedOrders = new ArrayList<>();

      getOrders(product, orderType, orders -> {
        int currentOrderIndex = orders.size() - 1;
        BazaarOrder currentOrder = orders.get(currentOrderIndex);

        if (compressedOrders.isEmpty()) {
          compressedOrders.add(new DefaultCompressedBazaarOrder(currentOrder));
          return true;
        }

        for (CompressedBazaarOrder compressedOrder : compressedOrders) {
          if (!compressedOrder.canAddOrder(currentOrder)) continue;
          compressedOrder.addOrder(currentOrder);
          return true;
        }

        if (compressedOrders.size() < limit) {
          compressedOrders.add(new DefaultCompressedBazaarOrder(currentOrder));
          return true;
        }

        orders.remove(currentOrder);
        return false;
      }).join();

      return compressedOrders;
    });
  }

  @Override
  public CompletableFuture<FillResult> fillOrders(Product product, OrderType orderType, int amount) {
    AtomicInteger currentAmount = new AtomicInteger();

    return getOrders(product, orderType, orders -> currentAmount.addAndGet(orders.get(orders.size() - 1).getOrderableItems()) < amount)
        .thenApply(orders -> {
          double price = Utils.getTotalPrice(orders, amount);

          int remainingAmount = amount;
          for (int i = 0; i < orders.size(); i++) {
            if (remainingAmount <= 0) break;

            BazaarOrder order = orders.get(i);
            int orderFillAmount = Math.min(remainingAmount, order.getOrderableItems());

            registerOrderFill(order, orderFillAmount).join();

            remainingAmount -= orderFillAmount;
          }

          return new DefaultFillResult(this, orders, Math.min(currentAmount.get(), amount), price);
        });
  }

  protected abstract CompletableFuture<Void> registerOrderFill(BazaarOrder order, int orderFillAmount);
}
