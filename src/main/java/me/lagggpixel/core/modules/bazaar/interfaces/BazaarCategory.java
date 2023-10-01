package me.lagggpixel.core.modules.bazaar.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public interface BazaarCategory {

    String getName();
    Material getIcon();
    ChatColor getColor();
    Short getPaneColor();

    List<BazaarItem> getItems();

}
