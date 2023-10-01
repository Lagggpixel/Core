package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;

import java.util.List;

@Data
public class SkyblockBazaarItem implements BazaarItem {

    private BazaarCategory category;
    private final String name;
    private final List<BazaarSubItem> subItems;
    private final int inventorySize;

}
