/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu.configurations;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.ProductCategory;
import me.lagggpixel.core.modules.bazaar.api.edit.EditManager;
import me.lagggpixel.core.modules.bazaar.bz.product.ProductConfiguration;
import me.lagggpixel.core.modules.bazaar.bz.product.ProductImpl;
import me.lagggpixel.core.modules.bazaar.config.BazaarConfig;
import me.lagggpixel.core.modules.bazaar.menu.DefaultConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.menu.MenuConfiguration;
import me.lagggpixel.core.modules.bazaar.utils.MenuUtils;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.Element;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class ProductCategoryMenuConfiguration extends MenuConfiguration {
  private final List<Integer> productSlots;

  public ProductCategoryMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items, List<Integer> productSlots) {
    super(name, rows, items);
    this.productSlots = productSlots;
  }


  public static ProductCategoryMenuConfiguration createDefaultProductCategoryConfiguration(String name, int products) {
    List<DefaultConfigurableMenuItem> items = new ArrayList<>();

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
    List<Integer> productSlots = new ArrayList<>();

    switch (products) {
      case 1:
        productSlots = Collections.singletonList(13);
        break;
      case 2:
        productSlots = Arrays.asList(12, 14);
        break;
      case 3:
        productSlots = Arrays.asList(11, 13, 15);
        break;
      case 4:
        productSlots = Arrays.asList(10, 12, 14, 16);
        break;
    }

    fillWithGlass(rows, items);

    return new ProductCategoryMenuConfiguration(name, rows, items, productSlots);
  }

  public static ProductCategoryMenuConfiguration deserialize(Map<String, Object> args) {
    return new ProductCategoryMenuConfiguration((String) args.get("name"),
        (Integer) args.get("rows"),
        (List<DefaultConfigurableMenuItem>) args.get("items"),
        (List<Integer>) args.get("slots"));
  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> args = super.serialize();

    args.put("slots", productSlots);

    return args;
  }

  public GUI getMenu(ProductCategory selectedCategory, boolean edit) {
    BazaarAPI bazaarApi = selectedCategory.getCategory().getBazaar().getBazaarApi();
    EditManager editManager = bazaarApi.getEditManager();

    return getMenuBuilder().prepare((gui, player) -> {
      super.loadItems(gui, bazaarApi, player, selectedCategory, edit);

      if (edit) {
        for (Map.Entry<Integer, Element> slotElementEntry : gui.content(null).entrySet()) {
          int slot = slotElementEntry.getKey();
          Element element = slotElementEntry.getValue();
          ItemStack originalItem = element.item(player);
          if (originalItem == null) continue;

          ItemStack item = ItemBuilder.newBuilder(originalItem)
              .appendLore(ChatColor.DARK_AQUA + "Middle-Click to set item here")
              .build();

          gui.setElement(slot, Component.element(item).click(clickInfo -> {
            if (clickInfo.getClickType() == ClickType.MIDDLE) {
              BazaarConfig bazaarConfig = ((BazaarModule) bazaarApi).getBazaarConfig();
              ProductConfiguration productConfiguration = bazaarConfig.getProductConfiguration(ItemBuilder.newBuilder(Material.COAL).withName(ChatColor.RED + "Not set!").build(), ChatColor.RED + "Not set!");
              ProductImpl product = new ProductImpl(selectedCategory, productConfiguration);

              productSlots.add(slot);
              selectedCategory.addProduct(product);
              editManager.openProductEdit(player, product);

              return;
            }
            element.click(clickInfo);
          }).build());
        }
      }

      List<Product> products = selectedCategory.getProducts();

      for (int i = 0; i < productSlots.size(); i++) {
        if (i >= products.size()) break;

        int slot = productSlots.get(i);
        Product product = products.get(i);

        int finalI = i;
        gui.setElement(slot, Component.element()
            .click(editManager.createEditableItemClickAction(clickInfo -> bazaarApi.getBazaar().openProduct(player, product),
                clickInfo -> bazaarApi.getBazaar().openProductEdit(player, product),
                clickInfo -> editManager.openProductEdit(player, product),
                clickInfo -> {
                  productSlots.remove(finalI);
                  selectedCategory.removeProduct(product);
                },
                clickInfo -> getMenu(selectedCategory, edit).open(player), edit))
            .item(MenuUtils.appendEditLore(product.getIcon(gui, slot, player), edit, true))
            .build());
      }
    }).build();
  }
}
