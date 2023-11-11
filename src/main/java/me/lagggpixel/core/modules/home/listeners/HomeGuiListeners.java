package me.lagggpixel.core.modules.home.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.home.handlers.HomeHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class HomeGuiListeners implements Listener {
  
  HomeHandler homeHandler;
  
  public HomeGuiListeners(HomeHandler homeHandler) {
    this.homeHandler = homeHandler;
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  
  @EventHandler(ignoreCancelled = true)
  public void handleHomeGUIClick(@NotNull InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    User user = Main.getUser(player.getUniqueId());
    
    Component menuName = event.getView().title();
    
    if (menuName != homeHandler.HOME_GUI_NAME) {
      return;
    }
    
    event.setCancelled(true);
    
    ItemStack clickedItem = event.getCurrentItem();
    
    if (clickedItem == null) {
      return;
    }
    
    String homeName = clickedItem.getItemMeta().getPersistentDataContainer().get(homeHandler.HOME_ITEM_NAMESPACE_KEY, PersistentDataType.STRING);
    
    Home home = user.getHomes().get(homeName);
    
    homeHandler.teleportToHome(player, home);
    player.closeInventory();
    
  }
}
