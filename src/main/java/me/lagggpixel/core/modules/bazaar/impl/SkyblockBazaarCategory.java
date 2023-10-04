package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

@Data
public class SkyblockBazaarCategory implements BazaarCategory {

    private final Component name;
    private final Material icon;
    private final TextColor color;

    private final List<BazaarItem> items;

}
