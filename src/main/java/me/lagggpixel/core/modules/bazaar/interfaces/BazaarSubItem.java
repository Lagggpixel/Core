package me.lagggpixel.core.modules.bazaar.interfaces;

import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BazaarSubItem {
  
  BazaarItem getParent();
  
  ItemStack getIcon();
  
  int getSlot();
  
  String getMaterial();
  
  List<BazaarOffer> getOrders();
  
  List<BazaarOffer> getOffers();
  
  double getLowestSellPrice();
  
  double getHighestBuyPrice();
  
  double getLowestSellPrice(int requiredAmount);
  
  double getHighestBuyPrice(int requiredAmount);
  
  default ItemBuilder getNamedIcon() {
    Component iconDisplayName = getIcon().getItemMeta().displayName();
    if (iconDisplayName == null) {{
      return new ItemBuilder(getIcon()).setDisplayName(ChatUtils.stringToComponent(StringUtils.capitalize(getIcon().getType().name().toLowerCase().replace("_", " ")))
              .color(getParent().getCategory().getColor())).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
    }}
    else {
      return new ItemBuilder(getIcon()).setDisplayName(iconDisplayName.color(getParent().getCategory().getColor())).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
    }
  }
  
  default ItemStack getItem() {
    return BazaarMiscUtil.getItem(getMaterial());
  }
  
}
