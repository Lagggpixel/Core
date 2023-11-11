package me.lagggpixel.core.modules.home.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HomeHandler {
  
  private final Component HOME_GUI_NAME = ChatUtils.stringToComponentCC("&aHomes");
  private final Component HOME_GUI_NAME_OTHER = ChatUtils.stringToComponentCC("&a%player%'s homes");
  public final NamespacedKey HOME_ITEM_NAMESPACE_KEY = new NamespacedKey(Main.getInstance(), "HOME_NAME");
  public final String HOME_PERMISSION_PREFIX = "coreplugin.home.command.max.";
  
  public void teleportToHome(Player player, Home home) {
    TeleportUtils.teleportWithDelay(player, home.location(), home.name() + " home");
  }
  
  public void setHome(User player, String homeName, Home home) {
    if (!player.getHomes().containsKey(homeName)) player.getHomes().put(homeName, home);
  }
  
  public void deleteHome(User player, String homeName) {
    player.getHomes().remove(homeName);
  }
  
  public Home createHomeObject(String homeName, Location location) {
    return new Home(homeName, location);
  }

  public void openHomesGUI(@NotNull Player player, @NotNull User user) {
    Inventory gui = player.getServer().createInventory(null, 27, HOME_GUI_NAME);

    for (Map.Entry<String, Home> homeEntry : user.getHomes().entrySet()) {
      ItemStack homeItem = createHomeItem(homeEntry.getKey());
      gui.addItem(homeItem);
    }

    player.openInventory(gui);
  }

  public void openHomesGUIOther(@NotNull Player player, @NotNull User target) {
    Inventory gui = player.getServer().createInventory(null, 27, HOME_GUI_NAME_OTHER.replaceText(TextReplacementConfig.builder().match("%player%").replacement(target.getPlayerName()).build()));

    for (Map.Entry<String, Home> homeEntry : target.getHomes().entrySet()) {
      ItemStack homeItem = createHomeItem(homeEntry.getKey());
      gui.addItem(homeItem);
    }

    player.openInventory(gui);
  }

  private @NotNull ItemStack createHomeItem(String homeName) {

    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta meta = item.getItemMeta();

    assert meta != null;
    meta.displayName(ChatUtils.stringToComponent(homeName));
    meta.getPersistentDataContainer().set(HOME_ITEM_NAMESPACE_KEY, PersistentDataType.STRING, homeName);

    item.setItemMeta(meta);

    return item;
  }

  public boolean homeNameInvalid(@NotNull String homeName) {
    int minLength = 3;
    int maxLength = 20;

    if (homeName.length() < minLength || homeName.length() > maxLength) {
      return true;
    }

    // Check if the home name contains special characters
    if (!homeName.matches("^[a-zA-Z0-9]+$")) {
      return true;
    }

    return false;
  }
}