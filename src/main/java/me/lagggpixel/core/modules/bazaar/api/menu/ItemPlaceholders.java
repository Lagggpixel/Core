package me.lagggpixel.core.modules.bazaar.api.menu;

import me.zort.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemPlaceholders {
    void addItemPlaceholder(ItemPlaceholderFunction action);

    ItemStack replaceItemPlaceholders(ContainerComponent containerComponent, ItemStack item, int itemSlot, Player player, MenuInfo info);
}
