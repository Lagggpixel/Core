package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

@Data
public class SkyblockBazaarCategory implements BazaarCategory {

    private final String name;
    private final Material icon;
    private final ChatColor color;
    private final Short paneColor;

    private final List<BazaarItem> items;

}
