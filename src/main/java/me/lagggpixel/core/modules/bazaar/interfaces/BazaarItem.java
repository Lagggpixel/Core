package me.lagggpixel.core.modules.bazaar.interfaces;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface BazaarItem {
  
  BazaarCategory getCategory();
  
  Component getName();
  
  List<BazaarSubItem> getSubItems();
  
  default int getInventorySize() {
    return 36;
  }
  
  default int getProductAmount() {
    return this.getSubItems().size();
  }
  
}
