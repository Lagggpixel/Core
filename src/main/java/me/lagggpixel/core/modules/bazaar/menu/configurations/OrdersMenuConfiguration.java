/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu.configurations;

import com.cryptomorin.xseries.XMaterial;
import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.menu.DefaultConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.menu.MenuConfiguration;
import me.lagggpixel.core.modules.bazaar.utils.MenuUtils;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.PagedContainer;
import me.lagggpixel.core.libs.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class OrdersMenuConfiguration extends MenuConfiguration {
    public OrdersMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static OrdersMenuConfiguration createDefaultConfiguration() {
        int[] glassSlots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 32, 33, 34, 35};

        List<DefaultConfigurableMenuItem> items = new ArrayList<>();
        for (int glassSlot : glassSlots) {
            items.add(new DefaultConfigurableMenuItem(glassSlot, ItemBuilder.newBuilder(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).withName(ChatColor.WHITE.toString()).build(), ""));
        }

        items.add(new DefaultConfigurableMenuItem(30,
                ItemBuilder.newBuilder(Material.ARROW)
                        .withName(ChatColor.GREEN + "Go Back")
                        .appendLore(ChatColor.GRAY + "To Bazaar")
                        .build(),
                "back"));
        items.add(new DefaultConfigurableMenuItem(31,
                ItemBuilder.newBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Close")
                        .build(),
                "close"));

        return new OrdersMenuConfiguration("Bazaar Orders", 4, items);
    }

    public static OrdersMenuConfiguration deserialize(Map<String, Object> args) {
        return new OrdersMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(BazaarAPI bazaarApi, boolean edit) {
        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, null, edit);

            PagedContainer ordersContainer = Component.pagedContainer()
                    .size(7, 2)
                    .init(container -> {
                        bazaarApi.getOrderManager().getUnclaimedOrders(player.getUniqueId()).thenAccept(orders -> {
                            for (BazaarOrder order : orders) {
                                container.appendElement(Component.element()
                                        .click(clickInfo -> {
                                            int claimed = bazaarApi.getOrderManager().claimOrder(order);
                                            if (claimed > 0) {
                                                clickInfo.close();
                                            }
                                        })
                                        .item(order.getIcon())
                                        .build());
                            }
                            MenuUtils.setPagingArrows(this, gui, container, bazaarApi, player, 28, 34, edit);
                            bazaarApi.getMenuHistory().refreshGui(player);
                        }).exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
                    })
                    .build();

            gui.setContainer(10, ordersContainer);
        }).build();
    }
}
