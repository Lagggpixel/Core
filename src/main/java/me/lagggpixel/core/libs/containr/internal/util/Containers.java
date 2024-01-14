package me.lagggpixel.core.libs.containr.internal.util;

import me.lagggpixel.core.libs.containr.Container;
import me.lagggpixel.core.libs.containr.StaticContainer;
import org.bukkit.inventory.Inventory;

public final class Containers {

    public static Container ofInv(Inventory inventory) {
        int width = 9;
        switch(inventory.getType()) {
            case HOPPER:
                width = 5;
                break;
            case CRAFTING:
            case DROPPER:
            case ANVIL:
                width = 3;
                break;
            case BEACON:
                width = 1;
                break;
        }
        return new StaticContainer(width, inventory.getSize() / width);
    }

}
