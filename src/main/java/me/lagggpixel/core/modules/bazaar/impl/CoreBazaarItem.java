package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import net.kyori.adventure.text.Component;

import java.util.List;

@Data
public class CoreBazaarItem implements BazaarItem {
  
  private BazaarCategory category;
  private final Component name;
  private final List<BazaarSubItem> subItems;
  private final int inventorySize;
  
}
