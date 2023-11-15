package me.lagggpixel.core.modules.bazaar.interfaces;

import me.lagggpixel.core.modules.bazaar.impl.CoreBazaarCategory;
import me.lagggpixel.core.modules.bazaar.impl.CoreBazaarItem;
import me.lagggpixel.core.modules.bazaar.impl.CoreBazaarSubItem;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.data.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface BazaarConfigIndexer {
  
  
  record BazaarConfigCategorySchema(Component name, Material icon, TextColor color,
                                    List<BazaarConfigCategoryItemSchema> items) {
    public BazaarCategory toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
      List<BazaarItem> bazaarItems = new ArrayList<>();
      
      for (BazaarConfigCategoryItemSchema item : this.items) {
        bazaarItems.add(item.toBazaarEquivalent());
      }
      
      CoreBazaarCategory category = new CoreBazaarCategory(this.name, this.icon, this.color, bazaarItems);
      
      for (BazaarItem item : bazaarItems) {
        ((CoreBazaarItem) item).setCategory(category);
      }
      
      return category;
    }
  }
  
  
  record BazaarConfigCategoryItemSchema(Component name, int inventorySize,
                                        List<BazaarConfigCategorySubItemSchema> subItems) {
    public BazaarItem toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
      List<BazaarSubItem> bazaarSubItems = new ArrayList<>();
      
      for (BazaarConfigCategorySubItemSchema subItem : this.subItems) {
        bazaarSubItems.add(subItem.toBazaarEquivalent());
      }
      
      BazaarItem item = new CoreBazaarItem(this.name, bazaarSubItems, this.inventorySize);
      
      for (BazaarSubItem subItem : bazaarSubItems) {
        ((CoreBazaarSubItem) subItem).setParent(item);
      }
      
      return item;
    }
  }
  
  
  record BazaarConfigCategorySubItemSchema(String material, int slot) {
    public BazaarSubItem toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
      ItemStack icon = BazaarMiscUtil.getItem(this.material);
      
      if (icon == null) {
        throw new Bazaar.BazaarItemNotFoundException("Could not find material: " + this.material);
      }
      
      return new CoreBazaarSubItem(icon, this.slot, this.material, new ArrayList<>(), new ArrayList<>());
    }
  }
  
  Pair<List<BazaarCategory>, List<BazaarSubItem>> index() throws Bazaar.BazaarIOException, Bazaar.BazaarItemNotFoundException;
  
  BazaarConfigCategorySchema indexCategory(Bazaar bazaar, JSONObject category);
  
  BazaarConfigCategoryItemSchema indexCategoryItem(Bazaar bazaar, JSONObject categoryItem);
  
  BazaarConfigCategorySubItemSchema indexCategorySubItem(Bazaar bazaar, JSONObject categorySubItem);
  
}
