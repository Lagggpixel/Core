package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.libs.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemPlaceholderFunction {
    ItemStack apply(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info);
}
