/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.SubmitResult;
import me.lagggpixel.core.modules.bazaar.databases.MySQLDatabase;
import me.lagggpixel.core.modules.bazaar.databases.SQLDatabase;
import me.lagggpixel.core.modules.bazaar.databases.SQLiteDatabase;
import me.lagggpixel.core.utils.ExceptionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class SQLOrderManager extends AbstractOrderManager {
  private final SQLDatabase sqlDatabase;

  public SQLOrderManager(BazaarModule module) {
    super(module);

    SQLDatabase.Type sqlType = module.getDatabaseConfig().getSqlType();
    if (sqlType == SQLDatabase.Type.MYSQL) {
      sqlDatabase = new MySQLDatabase(module.getDatabaseConfig().getMySQLConfig());
    } else {
      sqlDatabase = new SQLiteDatabase(Main.getInstance().getDataFolder(), "data/modules/bazaar/orders");
    }
    createTable();
  }

  private void createTable() {
    String autoIncrementSyntax = sqlDatabase.getType() == SQLDatabase.Type.SQLITE ? "AUTOINCREMENT" : "AUTO_INCREMENT";

    try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS orders (" +
        "id INTEGER PRIMARY KEY " + autoIncrementSyntax + ", " +
        "product_id VARCHAR(32) NOT NULL, " +
        "amount INTEGER NOT NULL, " +
        "unit_price DOUBLE NOT NULL, " +
        "order_type VARCHAR(16) NOT NULL, " +
        "player VARCHAR(36) NOT NULL, " +
        "filled INTEGER DEFAULT 0, " +
        "claimed INTEGER DEFAULT 0, " +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);")) {
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CompletableFuture<SubmitResult> registerBazaarOrder(BazaarOrder order) {
    return CompletableFuture.supplyAsync(() -> {
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("INSERT INTO orders (product_id, amount, unit_price, order_type, player, created_at) " +
          "VALUES (?, ?, ?, ?, ?, ?)")) {
        statement.setString(1, order.getProduct().getId());
        statement.setInt(2, order.getAmount());
        statement.setDouble(3, order.getUnitPrice());
        statement.setString(4, order.getType().name());
        statement.setString(5, String.valueOf(order.getPlayer()));
        statement.setTimestamp(6, Timestamp.from(order.getCreatedAt()));
        statement.executeUpdate();
        return SubmitResult.SUCCESS;
      } catch (SQLException e) {
        ExceptionUtils.handleException(e);
        return SubmitResult.ERROR;
      }
    });
  }

  @Override
  protected CompletableFuture<Void> registerClaim(BazaarOrder order, int claimed) {
    return CompletableFuture.runAsync(() -> {
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("UPDATE orders SET claimed=? WHERE id=?")) {
        statement.setInt(1, order.getClaimed() + claimed);
        statement.setInt(2, order.getDatabaseId());
        statement.executeUpdate();
      } catch (SQLException e) {
        ExceptionUtils.handleException(e);
      }
    });
  }

  @Override
  public CompletableFuture<List<BazaarOrder>> getOrders(Product product, OrderType type, Predicate<List<BazaarOrder>> shouldContinuePredicate) {
    return CompletableFuture.supplyAsync(() -> {
      String sortType = type == OrderType.BUY ? "DESC" : "ASC";
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("SELECT * FROM orders WHERE product_id=? AND order_type=? AND filled<amount ORDER BY unit_price " + sortType + ", created_at ASC")) {
        statement.setString(1, product.getId());
        statement.setString(2, type.name());

        List<BazaarOrder> orders = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            orders.add(new DefaultBazaarOrder(resultSet.getInt("id"),
                product,
                resultSet.getInt("amount"),
                resultSet.getDouble("unit_price"),
                type,
                UUID.fromString(resultSet.getString("player")),
                resultSet.getInt("filled"),
                resultSet.getInt("claimed"),
                resultSet.getTimestamp("created_at").toInstant()));
            if (!shouldContinuePredicate.test(orders)) {
              break;
            }
          }
        }

        return orders;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public CompletableFuture<List<BazaarOrder>> getUnclaimedOrders(UUID playerUniqueId) {
    return CompletableFuture.supplyAsync(() -> {
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("SELECT * FROM orders WHERE player=? AND claimed<amount ORDER BY created_at")) {
        statement.setString(1, playerUniqueId.toString());

        List<BazaarOrder> orders = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            orders.add(new DefaultBazaarOrder(resultSet.getInt("id"),
                module.getBazaar().getProduct(resultSet.getString("product_id")),
                resultSet.getInt("amount"),
                resultSet.getDouble("unit_price"),
                OrderType.valueOf(resultSet.getString("order_type")),
                playerUniqueId,
                resultSet.getInt("filled"),
                resultSet.getInt("claimed"),
                resultSet.getTimestamp("created_at").toInstant()));
          }
        }

        return orders;
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  protected CompletableFuture<Void> registerOrderFill(BazaarOrder order, int orderFillAmount) {
    return CompletableFuture.runAsync(() -> {
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("UPDATE orders SET filled=? WHERE id=?")) {
        statement.setInt(1, order.getFilled() + orderFillAmount);
        statement.setInt(2, order.getDatabaseId());
        statement.executeUpdate();
      } catch (SQLException e) {
        ExceptionUtils.handleException(e);
      }
    });
  }

  @Override
  public CompletableFuture<Void> undoOrderFill(BazaarOrder previousOrder) {
    return CompletableFuture.runAsync(() -> {
      try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("UPDATE orders SET filled=? WHERE id=?")) {
        statement.setInt(1, previousOrder.getFilled());
        statement.setInt(2, previousOrder.getDatabaseId());
        statement.executeUpdate();
      } catch (SQLException e) {
        ExceptionUtils.handleException(e);
      }
    });
  }
}
