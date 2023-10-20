package me.lagggpixel.core.modules.bazaar.interfaces;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

public interface BazaarCategory {
  
  Component getName();
  
  Material getIcon();
  
  TextColor getColor();
  
  List<BazaarItem> getItems();
  
}
