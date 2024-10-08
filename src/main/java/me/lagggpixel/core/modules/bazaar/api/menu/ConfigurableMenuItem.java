/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.libs.containr.ContainerComponent;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @since January 22, 2024
 */
public interface ConfigurableMenuItem extends ConfigurationSerializable {
  int getSlot();

  void setSlot(Bazaar bazaar, int slot);

  ItemStack getItem();

  void setItem(Bazaar bazaar, ItemStack item);

  String getAction();

  void setAction(Bazaar bazaar, String action);

  void putItem(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info, boolean editMenu);
}
