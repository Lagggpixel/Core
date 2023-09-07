package me.lagggpixel.core.modules.homes.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.homes.data.Home;
import me.lagggpixel.core.modules.homes.managers.HomeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class HomeGuiListeners implements Listener {

    HomeManager homeManager;

    public HomeGuiListeners(HomeManager homeManager) {
        this.homeManager = homeManager;
        Main.getPluginManager().registerEvents(this, Main.getInstance());
    }


    @EventHandler(ignoreCancelled = true)
    public void handleHomeGUIClick(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        User user = Main.getUser(player.getUniqueId());

        Component menuName = event.getView().title();

        if (menuName != homeManager.HOME_GUI_NAME) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        String homeName = clickedItem.getItemMeta().getPersistentDataContainer().get(homeManager.HOME_ITEM_NAMESPACE_KEY, PersistentDataType.STRING);

        Home home = user.getHomes().get(homeName);

        homeManager.teleportToHome(player, home);
        player.closeInventory();

    }
}
