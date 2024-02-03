/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.merchant.data;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.Pair;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ExceptionUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Exortions
 * @since January 22, 2024
 */
@SuppressWarnings("unused")
@Getter
public class MerchantGui implements Listener {

  private static final HashMap<MerchantGui, Boolean> REGISTERED_LISTENERS = new HashMap<>();

  public final HashMap<ItemStack, Pair<Runnable, Boolean>> specificClickEvents;
  public final HashMap<Component, Pair<Runnable, Boolean>> clickEvents;
  public final HashMap<Integer, ItemStack> items;
  public final HashMap<Player, Boolean> opened;
  public final List<ItemStack> addableItems;
  public final int slots;
  public Component name;

  public MerchantGui(Component name, int slots, HashMap<Component, Pair<Runnable, Boolean>> clickEvents) {
    this(name, slots, clickEvents, new HashMap<>());
  }

  private static class AbstractCommandGui extends MerchantGui {
    private final String command;

    @SuppressWarnings("unused")
    public AbstractCommandGui(String command, Player opener) {
      super(ChatUtils.stringToComponentCC("Command"), 9, new HashMap<>());

      this.command = command;
    }
  }

  private static final HashMap<Component, Class<? extends MerchantGui>> BACK_BUTTONS = new HashMap<>();

  public MerchantGui(Component name, int slots, HashMap<Component, Pair<Runnable, Boolean>> clickEvents, HashMap<ItemStack, Pair<Runnable, Boolean>> specificClickEvents) {
    this.name = name;
    this.slots = slots;

    this.clickEvents = clickEvents;
    this.specificClickEvents = specificClickEvents;

    this.items = new HashMap<>();
    this.addableItems = new ArrayList<>();
    this.opened = new HashMap<>();
  }

  public void show(Player player) {
    if (this instanceof AbstractCommandGui) {
      player.performCommand(((AbstractCommandGui) this).command);
      return;
    }

    Inventory inventory = player.getServer().createInventory(null, slots, name);

    for (int i = 0; i < slots; i++) {
      if (items.containsKey(i)) {
        inventory.setItem(i, items.get(i));
      }
    }

    for (ItemStack stack : this.addableItems) inventory.addItem(stack);

    player.openInventory(inventory);

    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

    opened.put(player, true);
  }

  public void hide(Player player) {
    player.closeInventory();
  }

  public void addItem(int slot, ItemStack stack) {
    this.items.put(slot, stack);
  }

  public void addItem(ItemStack stack) {
    this.addableItems.add(stack);
  }

  public ItemStack getItem(int slot) {
    return this.items.get(slot);
  }

  protected boolean getSpecificClickSound() {
    return true;
  }

  public void fillEmpty(ItemStack stack) {
    for (int i = 0; i < this.slots; i++) {
      if (!this.items.containsKey(i)) this.items.put(i, stack);
    }
  }

  @EventHandler
  public void onClick(@NotNull InventoryClickEvent event) {
    if (event.getView().title().equals(name) && opened.containsKey((Player) event.getWhoClicked())) {
      event.setCancelled(true);

      Player player = (Player) event.getWhoClicked();
      User user = Main.getUser(player);

      onInventoryClick(event);

      ItemStack currenItem = event.getCurrentItem();
      if (currenItem == null) {
        return;
      }
      if (!currenItem.hasItemMeta()) {
        return;
      }
      ItemMeta currenItemMeta = currenItem.getItemMeta();
      Component displayName = currenItemMeta.displayName();
      if (displayName == null) {
        return;
      }

      if (displayName.equals(buildCloseButton().getItemMeta().displayName())) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        event.getWhoClicked().closeInventory();
        return;
      }

      List<Component> lore = event.getCurrentItem().getItemMeta().lore();

      if (lore != null
          && !lore.isEmpty()
          && BACK_BUTTONS.containsKey(lore.get(0))) {
        Class<? extends MerchantGui> clazz = BACK_BUTTONS.get(lore.get(0));

        try {
          //noinspection RedundantCast
          clazz.getConstructor(Player.class).newInstance((Player) (event.getWhoClicked())).show((Player) event.getWhoClicked());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
          ExceptionUtils.handleException(e);
        }

        return;
      }

      if (specificClickEvents.containsKey(event.getCurrentItem())) {
        specificClickEvents.get(event.getCurrentItem()).getFirst().run();
        if (specificClickEvents.get(event.getCurrentItem()).getSecond()) {
          player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
        return;
      }

      if (clickEvents.containsKey(event.getCurrentItem().getItemMeta().displayName())) {
        clickEvents.get(event.getCurrentItem().getItemMeta().displayName()).getFirst().run();
        if (clickEvents.get(event.getCurrentItem().getItemMeta().displayName()).getSecond()) {
          player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
      }
    }
  }

  @EventHandler
  public void onClose(@NotNull InventoryCloseEvent e) {
    if (e.getView().title().equals(name) && opened.containsKey((Player) e.getPlayer())) {
      onClose((Player) e.getPlayer());

      HandlerList.unregisterAll(this);
      opened.remove((Player) e.getPlayer());
    }
  }

  public void onClose(Player p) {
  }

  public void onInventoryClick(InventoryClickEvent e) {
  }

  private ItemStack buildCloseButton() {
    NBTItem item = new NBTItem(new ItemBuilder(Material.BARRIER).setDisplayName("&cClose").toItemStack());

    item.setBoolean("close", true);

    return item.getItem();
  }
}