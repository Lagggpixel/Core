/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu.configurations;

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Category;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.menu.DefaultConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.menu.MenuConfiguration;
import me.lagggpixel.core.modules.bazaar.utils.MenuUtils;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.PagedContainer;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class SearchMenuConfiguration extends MenuConfiguration {
    public SearchMenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items) {
        super(name, rows, items);
    }

    public static SearchMenuConfiguration createDefaultConfiguration(String name, ItemStack glass) {
        return new SearchMenuConfiguration(name, 6, CategoryMenuConfiguration.getDefaultCategoryMenuItems(glass));
    }

    public static SearchMenuConfiguration deserialize(Map<String, Object> args) {
        return new SearchMenuConfiguration((String) args.get("name"),
                (Integer) args.get("rows"),
                (List<DefaultConfigurableMenuItem>) args.get("items"));
    }

    public GUI getMenu(Bazaar bazaar, String filter, boolean edit) {
        BazaarAPI bazaarApi = bazaar.getBazaarApi();

        return getMenuBuilder().prepare((gui, player) -> {
            super.loadItems(gui, bazaarApi, player, null, edit);

            gui.setContainer(0, Component.staticContainer()
                    .size(1, 5)
                    .init(container -> {
                        for (Category category : bazaar.getCategories()) {
                            container.appendElement(Component.element()
                                    .click(element -> {
                                        bazaar.open(player, category);
                                    })
                                    .item(category.getIcon())
                                    .build());
                        }
                    })
                    .build());

            PagedContainer productCategoriesContainer = Component.pagedContainer()
                    .size(6, 4)
                    .init(container -> {
                        List<Product> products = bazaar.getProducts(product -> product.getName().toLowerCase().contains(filter.toLowerCase()));
                        for (Product product : products) {
                            container.appendElement(Component.element()
                                    .click(element -> bazaar.openProduct(player, product))
                                    .item(product.getIcon(container, MenuUtils.getNextFreeSlot(container), player))
                                    .build());
                        }
                    })
                    .build();

            gui.setContainer(11, productCategoriesContainer);
            MenuUtils.setPagingArrows(this, gui, productCategoriesContainer, bazaarApi, player, 48, 51, edit);
        }).build();
    }

}
