/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.home.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.home.handlers.HomeHandler;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class HomeGuiListeners implements Listener {

  HomeHandler homeHandler;

  public HomeGuiListeners(HomeHandler homeHandler) {
    this.homeHandler = homeHandler;
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }


  @EventHandler(ignoreCancelled = true)
  public void handleHomeGUIClick(@NotNull InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Component menuName = event.getView().title();
    String stringMenuName = ChatUtils.componentToString(menuName);

    if (menuName == homeHandler.HOME_GUI_NAME) {
      event.setCancelled(true);

      ItemStack clickedItem = event.getCurrentItem();

      if (clickedItem == null) {
        return;
      }

      String homeName = clickedItem.getItemMeta().getPersistentDataContainer().get(homeHandler.HOME_ITEM_NAMESPACE_KEY_NAME, PersistentDataType.STRING);
      User user = Main.getUser(player.getUniqueId());
      Home home = user.getHomes().get(homeName);

      homeHandler.teleportToHome(player, home);
      player.closeInventory();
      return;
    }

    if (stringMenuName.contains(homeHandler.HOME_GUI_NAME_FILTER)) {
      event.setCancelled(true);
      ItemStack clickedItem = event.getCurrentItem();

      if (clickedItem == null) {
        return;
      }
      String stringUUID = clickedItem.getItemMeta().getPersistentDataContainer().get(homeHandler.HOME_ITEM_NAMESPACE_KEY_UUID, PersistentDataType.STRING);
      if (stringUUID == null) {
        return;
      }
      User target = Main.getUser(UUID.fromString(stringUUID));
      String homeName = clickedItem.getItemMeta().getPersistentDataContainer().get(homeHandler.HOME_ITEM_NAMESPACE_KEY_NAME, PersistentDataType.STRING);
      Home home = target.getHomes().get(homeName);

      homeHandler.teleportToOthersHome(player, target, home);
      return;
    }

  }
}
