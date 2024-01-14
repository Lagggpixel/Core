package me.lagggpixel.core.modules.bazaar.menu;

import io.github.rapha149.signgui.SignGUI;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;
import me.lagggpixel.core.modules.bazaar.api.menu.ClickActionManager;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.libs.containr.ContextClickInfo;
import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultClickActionManager implements ClickActionManager {
  private final BazaarModule module;
  private final Map<String, Function<MenuInfo, Consumer<ContextClickInfo>>> clickActions = new HashMap<>();
  private final Map<String, BiFunction<ConfigurableMenuItem, MenuInfo, Consumer<ContextClickInfo>>> editActions = new HashMap<>();
  
  public DefaultClickActionManager(BazaarModule module) {
    this.module = module;
    
    addClickActions();
    addEditClickActions();
  }
  
  private void addClickActions() {
    addClickAction("close", ContextClickInfo::close);
    addClickAction("back", (Consumer<ContextClickInfo>) clickInfo -> module.getMenuHistory().openPrevious(clickInfo.getPlayer()));
    addClickAction("search", clickInfo -> {
      clickInfo.getPlayer().closeInventory();
      
      SignGUI.builder()
          .setLines(module.getMenuConfig().getStringList("search-sign").toArray(new String[4]))
          .setHandler((player, lines) -> {
            String filter = lines.getLine(0);
            module.getBazaar().openSearch(player, filter);
            return null;
          })
          .build()
          .open(clickInfo.getPlayer());
    });
    addClickAction("buy-order", menuInfo -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      requireNumberFromPlayer(clickInfo.getPlayer(), "buy-order-amount-sign", amount -> {
        requireNumberFromPlayer(clickInfo.getPlayer(), "buy-order-price-sign", unitPrice -> {
          BazaarOrder order = module.getOrderManager().prepareBazaarOrder(product, amount, unitPrice, OrderType.BUY, clickInfo.getPlayer().getUniqueId());
          module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.BUY).getMenu(order, false).open(clickInfo.getPlayer());
        }, Double::parseDouble);
      }, Integer::parseInt);
    });
    addClickAction("sell-offer", menuInfo -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      requireNumberFromPlayer(clickInfo.getPlayer(), "sell-offer-amount-sign", amount -> {
        requireNumberFromPlayer(clickInfo.getPlayer(), "sell-offer-price-sign", unitPrice -> {
          BazaarOrder order = module.getOrderManager().prepareBazaarOrder(product, amount, unitPrice, OrderType.SELL, clickInfo.getPlayer().getUniqueId());
          module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getMenu(order, false).open(clickInfo.getPlayer());
        }, Double::parseDouble);
      }, Integer::parseInt);
    });
    
    addClickAction("buy-instantly", menuInfo -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      requireNumberFromPlayer(clickInfo.getPlayer(), "buy-instantly-amount-sign", amount -> {
        module.getOrderManager().prepareInstantOrder(product, amount, OrderType.BUY, clickInfo.getPlayer().getUniqueId()).thenAccept(order -> {
          module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.BUY).getInstantMenu(order, false).open(clickInfo.getPlayer());
        });
      }, Integer::parseInt);
    });
    
    addClickAction("sell-instantly", menuInfo -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      if (clickInfo.getClickType().isRightClick()) {
        requireNumberFromPlayer(clickInfo.getPlayer(), "sell-instantly-amount-sign", amount -> {
          module.getOrderManager().prepareInstantOrder(product, amount, OrderType.SELL, clickInfo.getPlayer().getUniqueId()).thenAccept(order -> {
            module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getInstantMenu(order, false).open(clickInfo.getPlayer());
          });
        }, Integer::parseInt);
        return;
      }
      
      int amount = module.getBazaar().getProductAmountInInventory(product, clickInfo.getPlayer());
      module.getOrderManager().prepareInstantOrder(product, amount, OrderType.SELL, clickInfo.getPlayer().getUniqueId()).thenAccept(order -> {
        module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getInstantMenu(order, false).open(clickInfo.getPlayer());
      });
    });
    
    addClickAction("reject-order", menuInfo -> clickInfo -> {
      if (menuInfo instanceof BazaarOrder) {
        clickInfo.getPlayer().sendMessage(Lang.BAZAAR_ORDER_REJECT.toComponentWithPrefix());
        clickInfo.close();
        return;
      }
      
      if (menuInfo instanceof InstantBazaarOrder) {
        clickInfo.getPlayer().sendMessage(Lang.BAZAAR_INSTANT_REJECT.toComponentWithPrefix());
        clickInfo.close();
      }
    });
    
    addClickAction("confirm-order", menuInfo -> clickInfo -> {
      if (menuInfo instanceof BazaarOrder order) {
        module.getOrderManager().submitBazaarOrder(order);
        clickInfo.close();
      }
      
      if (menuInfo instanceof InstantBazaarOrder order) {
        module.getOrderManager().submitInstantOrder(order);
        clickInfo.close();
      }
    });
    
    addClickAction("manage-orders", clickInfo -> {
      module.getBazaar().openOrders(clickInfo.getPlayer());
    });
    
    addClickAction("sell-inventory", menuInfo -> clickInfo -> {
      Map<Product, Integer> productsInInventory = module.getBazaar().getProductsInInventory(clickInfo.getPlayer());
      
      for (Map.Entry<Product, Integer> productAmountEntry : productsInInventory.entrySet()) {
        Product product = productAmountEntry.getKey();
        int playerAmount = productAmountEntry.getValue();
        
        module.getOrderManager().prepareInstantOrder(product, playerAmount, OrderType.SELL, clickInfo.getPlayer().getUniqueId())
            .thenAccept(order -> module.getOrderManager().submitInstantOrder(order));
      }
    });
    
    addClickAction("", clickInfo -> {
    });
  }
  
  private void addEditClickActions() {
    addEditClickAction("search", (configurableMenuItem, menuInfo) -> clickInfo -> {
      clickInfo.getPlayer().closeInventory();
      
      SignGUI.builder()
          .setLines(module.getMenuConfig().getStringList("search-sign").toArray(new String[4]))
          .setHandler((player, lines) -> {
            String filter = lines.getLine(0);
            module.getBazaar().openEditSearch(player, filter, configurableMenuItem);
            return null;
          })
          .build()
          .open(clickInfo.getPlayer());
    });
    
    addEditClickAction("manage-orders", (configurableMenuItem, menuInfo) -> clickInfo -> {
      module.getBazaar().openEditOrders(clickInfo.getPlayer());
    });
    
    
    addEditClickAction("buy-order", (configurableMenuItem, menuInfo) -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      BazaarOrder order = module.getOrderManager().prepareBazaarOrder(product, 0, 0, OrderType.BUY, clickInfo.getPlayer().getUniqueId());
      module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.BUY).getMenu(order, true).open(clickInfo.getPlayer());
    });
    addEditClickAction("sell-offer", (configurableMenuItem, menuInfo) -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      BazaarOrder order = module.getOrderManager().prepareBazaarOrder(product, 0, 0, OrderType.SELL, clickInfo.getPlayer().getUniqueId());
      module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getMenu(order, true).open(clickInfo.getPlayer());
    });
    
    
    addEditClickAction("buy-instantly", (configurableMenuItem, menuInfo) -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      module.getOrderManager().prepareInstantOrder(product, 0, OrderType.BUY, clickInfo.getPlayer().getUniqueId()).thenAccept(order -> {
        module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.BUY).getInstantMenu(order, true).open(clickInfo.getPlayer());
      });
    });
    
    addEditClickAction("sell-instantly", (configurableMenuItem, menuInfo) -> clickInfo -> {
      if (!(menuInfo instanceof Product product)) return;
      
      module.getOrderManager().prepareInstantOrder(product, 0, OrderType.SELL, clickInfo.getPlayer().getUniqueId()).thenAccept(order -> {
        module.getBazaarConfig().getConfirmationMenuConfiguration(OrderType.SELL).getInstantMenu(order, true).open(clickInfo.getPlayer());
      });
    });
  }
  
  @Override
  public void addClickAction(String name, Consumer<ContextClickInfo> action) {
    addClickAction(name, menuInfo -> action);
  }
  
  @Override
  public void addClickAction(String name, Function<MenuInfo, Consumer<ContextClickInfo>> action) {
    clickActions.put(name, action);
  }
  
  @Override
  public void addEditClickAction(String name, BiFunction<ConfigurableMenuItem, MenuInfo, Consumer<ContextClickInfo>> action) {
    editActions.put(name, action);
  }
  
  @Override
  public Consumer<ContextClickInfo> getClickAction(ConfigurableMenuItem configurableMenuItem, MenuInfo menuInfo, boolean editing) {
    if (editing) {
      return clickInfo -> {
        if (clickInfo.getClickType().isRightClick()) {
          module.getEditManager().openItemEdit(clickInfo.getPlayer(), configurableMenuItem);
          return;
        }
        
        editActions.getOrDefault(configurableMenuItem.getAction(),
                (configurableMenuItem1, menuInfo1) -> getClickAction(configurableMenuItem, menuInfo, false))
            .apply(configurableMenuItem, menuInfo).accept(clickInfo);
      };
    }
    return clickActions.getOrDefault(configurableMenuItem.getAction(), menuInfo1 -> clickInfo -> {
    }).apply(menuInfo);
  }
  
  @Override
  public Set<String> getActions() {
    return clickActions.keySet();
  }
  
  private <T extends Number> void requireNumberFromPlayer(Player player, String sign, Consumer<T> callback, Function<String, T> parser) {
    Stack<GUI> history = module.getMenuHistory().getHistory(player);
    player.closeInventory();
    
    SignGUI.builder()
        .setLines(module.getMenuConfig().getStringList(sign).toArray(new String[4]))
        .setHandler((signPlayer, lines) -> {
          try {
            T amount = parser.apply(lines.getLine(0));
            
            if (amount.doubleValue() <= 0) {
              GUI lastGui = history.pop();
              module.getMenuHistory().setHistory(player, history);
              lastGui.open(player);
              return null;
            }
            
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
              module.getMenuHistory().setHistory(player, history);
              callback.accept(amount);
            }, 1);
          } catch (NumberFormatException exception) {
            GUI lastGui = history.pop();
            module.getMenuHistory().setHistory(player, history);
            lastGui.open(player);
          }
          return null;
        })
        .build()
        .open(player);
  }
}
