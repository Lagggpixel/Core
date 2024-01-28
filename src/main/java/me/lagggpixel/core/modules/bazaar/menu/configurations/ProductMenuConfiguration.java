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
 * @since January 27, 2024 January 22, 2024
 */
public class ProductMenuConfiguration extends MenuConfiguration {
    private final int productSlot;

    public ProductMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items, int productSlot) {
        super(name, rows, items);
        this.productSlot = productSlot;
    }


    public static ProductMenuConfiguration createDefaultProductConfiguration() {
        List<DefaultConfigurableMenuItem> items = new ArrayList<>();

        items.add(new DefaultConfigurableMenuItem(10,
                ItemBuilder.newBuilder(XMaterial.GOLDEN_HORSE_ARMOR.parseMaterial())
                        .withName(ChatColor.GREEN + "Buy Instantly")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore("%buy-instantly%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to pick amount!")
                        .build(),
                "buy-instantly"));
        items.add(new DefaultConfigurableMenuItem(11,
                ItemBuilder.newBuilder(Material.HOPPER)
                        .withName(ChatColor.GOLD + "Sell Instantly")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore("%sell-instantly%")
                        .appendLore("")
                        .appendLore(ChatColor.AQUA + "Right-Click to pick amount!")
                        .appendLore(ChatColor.YELLOW + "Click to sell!")
                        .build(),
                "sell-instantly"));

        items.add(new DefaultConfigurableMenuItem(15,
                ItemBuilder.newBuilder(Material.MAP)
                        .withName(ChatColor.GREEN + "Create Buy Order")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore(ChatColor.GREEN + "Top Orders:")
                        .appendLore("%buy-orders%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to setup Buy Order!")
                        .build(),
                "buy-order"));
        items.add(new DefaultConfigurableMenuItem(16,
                ItemBuilder.newBuilder(XMaterial.MAP.parseMaterial())
                        .withName(ChatColor.GOLD + "Create Sell Offer")
                        .appendLore("%product%")
                        .appendLore("")
                        .appendLore(ChatColor.GOLD + "Top Offers:")
                        .appendLore("%sell-offers%")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to setup Sell Offer!")
                        .build(),
                "sell-offer"));

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
        items.add(new DefaultConfigurableMenuItem(32,
                ItemBuilder.newBuilder(Material.BOOK)
                        .withName(ChatColor.GREEN + "Manage Orders")
                        .appendLore(ChatColor.GRAY + "You don't have any ongoing")
                        .appendLore(ChatColor.GRAY + "orders.")
                        .appendLore("")
                        .appendLore(ChatColor.YELLOW + "Click to manage!")
                        .build(),
                "manage-orders"));

        int rows = 4;

        fillWithGlass(rows, items);

        return new ProductMenuConfiguration("%product%", rows, items, 13);
    }

    public static ProductMenuConfiguration deserialize(Map<String, Object> args) {
        return new ProductMenuConfiguration((String) args.get("name"),
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

    public GUI getMenu(Product product, boolean edit) {
        BazaarAPI bazaarApi = product.getProductCategory().getCategory().getBazaar().getBazaarApi();

        return getMenuBuilder()
                .title(name.replace("%product%", product.getName()))
                .prepare((gui, player) -> {
                    super.loadItems(gui, bazaarApi, player, product, edit);

                    gui.setElement(productSlot, Component.element()
                            .click(element -> {
                            })
                            .item(product.getIcon(gui, productSlot, player))
                            .build());
                }).build();
    }
}
