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
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;
import me.lagggpixel.core.modules.bazaar.menu.DefaultConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.menu.MenuConfiguration;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class ConfirmationMenuConfiguration extends MenuConfiguration {
    private final int productSlot;

    public ConfirmationMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items, int productSlot) {
        super(name, rows, items);
        this.productSlot = productSlot;
    }

    public static ConfirmationMenuConfiguration createDefaultConfirmationConfiguration(OrderType type) {
        List<DefaultConfigurableMenuItem> items = new ArrayList<>();

        items.add(new DefaultConfigurableMenuItem(11,
                ItemBuilder.newBuilder(XMaterial.matchXMaterial("STAINED_CLAY:14").get().parseItem())
                        .withName(ChatColor.RED + "Reject Order")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to reject this order!")
                        .build(),
                "reject-order"));

        items.add(new DefaultConfigurableMenuItem(15,
                ItemBuilder.newBuilder(XMaterial.matchXMaterial("STAINED_CLAY:5").get().parseItem())
                        .withName(ChatColor.GREEN + "Confirm Order")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to confirm this order!")
                        .build(),
                "confirm-order"));

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

        int rows = 4;
        fillWithGlass(rows, items);

        String orderTypeText = type == OrderType.SELL ? "Sell" : "Buy";

        return new ConfirmationMenuConfiguration("Confirm " + orderTypeText + " Order", rows, items, 13);
    }

    public static ConfirmationMenuConfiguration deserialize(Map<String, Object> args) {
        return new ConfirmationMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"),
                (int) args.get("slot"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = super.serialize();

        args.put("slot", productSlot);

        return args;
    }

    public GUI getMenu(BazaarOrder order, boolean edit) {
        Product product = order.getProduct();
        BazaarAPI bazaarApi = product.getProductCategory().getCategory().getBazaar().getBazaarApi();

        return getMenuBuilder()
                .title(name.replace("%product%", product.getName()))
                .prepare((gui, player) -> {
                    super.loadItems(gui, bazaarApi, player, order, edit);

                    gui.setElement(productSlot, Component.element()
                            .click(clickInfo -> {
                                bazaarApi.getOrderManager().submitBazaarOrder(order);
                                clickInfo.close();
                            })
                            .item(product.getConfirmationIcon(order.getUnitPrice(), order.getAmount()))
                            .build());
                }).build();
    }

    public GUI getInstantMenu(InstantBazaarOrder order, boolean edit) {
        Product product = order.getProduct();
        BazaarAPI bazaarApi = product.getProductCategory().getCategory().getBazaar().getBazaarApi();

        return getMenuBuilder()
                .title(name.replace("%product%", product.getName()))
                .prepare((gui, player) -> {
                    super.loadItems(gui, bazaarApi, player, order, edit);

                    gui.setElement(productSlot, Component.element()
                            .click(clickInfo -> {
                                bazaarApi.getOrderManager().submitInstantOrder(order);
                                clickInfo.close();
                            })
                            .item(order.getIcon())
                            .build());
                }).build();
    }
}