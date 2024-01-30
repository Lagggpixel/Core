/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.bazaar.api;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderManager;
import me.lagggpixel.core.modules.bazaar.api.config.MenuConfig;
import me.lagggpixel.core.modules.bazaar.api.edit.EditManager;
import me.lagggpixel.core.modules.bazaar.api.menu.ClickActionManager;
import me.lagggpixel.core.modules.bazaar.api.menu.ItemPlaceholders;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuHistory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @since January 22, 2024
 */
public interface BazaarAPI {
  Economy getEconomy();

  MenuConfig getMenuConfig();

  Bazaar getBazaar();

  ClickActionManager getClickActionManager();

  ItemPlaceholders getItemPlaceholders();

  OrderManager getOrderManager();

  MenuHistory getMenuHistory();

  EditManager getEditManager();

  JavaPlugin getPlugin();
}
