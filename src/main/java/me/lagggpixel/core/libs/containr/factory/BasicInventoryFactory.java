package me.lagggpixel.core.libs.containr.factory;

import lombok.RequiredArgsConstructor;
import me.lagggpixel.core.libs.containr.InventoryInfo;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class BasicInventoryFactory implements InventoryFactory {

    private final String title;
    private final int rows;

    @Override
    public InventoryInfo createInventory(GUI gui) {
        Inventory inventory = Bukkit.createInventory(gui, rows * 9, title);
        return new InventoryInfo(inventory, title);
    }
}
