package me.lagggpixel.core.modules.home.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class HomeHandler {
  
  public final Component HOME_GUI_NAME = ChatUtils.stringToComponentCC("&aHomes");
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
}