package me.lagggpixel.core.modules.bazaar.utils.gui;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.gui.BazaarCategoryBazaarGui;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

@Getter
public class BazaarGui implements Listener {

  private static final HashMap<BazaarGui, Boolean> REGISTERED_LISTENERS = new HashMap<>();

  public final HashMap<ItemStack, Runnable> specificClickEvents;
  public final HashMap<Component, Runnable> clickEvents;
  public final HashMap<Integer, ItemStack> items;
  public final HashMap<Player, Boolean> opened;
  public final List<ItemStack> addableItems;
  public final int slots;
  public Component name;

  public BazaarGui(Component name, int slots, HashMap<Component, Runnable> clickEvents) {
    this(name, slots, clickEvents, new HashMap<>());
  }

  private static final HashMap<String, Class<? extends BazaarGui>> BACK_BUTTONS = new HashMap<>() {{
    put("To Bazaar", BazaarCategoryBazaarGui.class);
  }};

  public BazaarGui(Component name, int slots, HashMap<Component, Runnable> clickEvents, HashMap<ItemStack, Runnable> specificClickEvents) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.name = name;
    this.slots = slots;

    this.clickEvents = clickEvents;
    this.specificClickEvents = specificClickEvents;

    this.items = new HashMap<>();
    this.addableItems = new ArrayList<>();
    this.opened = new HashMap<>();
  }

  public void show(Player player) {
    Inventory inventory = player.getServer().createInventory(null, slots, name);

    for (int i = 0; i < slots; i++) {
      if (items.containsKey(i)) {
        inventory.setItem(i, items.get(i));
      }
    }

    for (ItemStack stack : this.addableItems) inventory.addItem(stack);

    player.closeInventory();
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

  public void fillEmpty(ItemStack stack) {
    for (int i = 0; i < this.slots; i++) {
      if (!this.items.containsKey(i)) this.items.put(i, stack);
    }
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (event.getView().title().equals(name) && opened.containsKey((Player) event.getWhoClicked())) {

      if (event.getCurrentItem() == null) {
        return;
      }

      event.setCancelled(true);
      onInventoryClick(event);

      if (event.getCurrentItem() == null) return;
      if (!event.getCurrentItem().hasItemMeta()) return;
      if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

      // Close button logic
      Component displayName = event.getCurrentItem().getItemMeta().displayName();
      if (displayName != null && displayName.equals(BazaarMiscUtil.buildCloseButton().getItemMeta().displayName())) {
        event.getWhoClicked().closeInventory();
        return;
      }

      // Back button logic
      List<Component> lore = event.getCurrentItem().getItemMeta().lore();
      if (lore != null && !lore.isEmpty() && BACK_BUTTONS.containsKey(ChatUtils.componentToString(lore.get(0)))) {
        Class<? extends BazaarGui> clazz = BACK_BUTTONS.get(ChatUtils.componentToString(lore.get(0)));

        try {
          if (event.getWhoClicked() instanceof Player player) {
            clazz.getConstructor(Player.class).newInstance(player).show(player);
          }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
          Main.log(Level.SEVERE, "Failed to create instance of " + clazz.getName() + ".");
          Main.log(Level.SEVERE, e.getMessage());
        }
      }
    }
  }

  @EventHandler
  public void onClose(InventoryCloseEvent e) {
    if (e.getView().title().equals(name) && opened.containsKey((Player) e.getPlayer())) {
      onClose((Player) e.getPlayer());

      HandlerList.unregisterAll(this);
      opened.remove((Player) e.getPlayer());
    }
  }

  public void onClose(Player p) {
  }

  public void onInventoryClick(InventoryClickEvent e) {

    if (e.getCurrentItem() != null) {

      Component name = e.getCurrentItem().getItemMeta().displayName();

      if (clickEvents.containsKey(name)) {
        clickEvents.get(name).run();
      }

    }


  }
}
