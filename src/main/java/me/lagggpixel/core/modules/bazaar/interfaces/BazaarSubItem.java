package me.lagggpixel.core.modules.bazaar.interfaces;

import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ItemBuilder;
import org.apache.commons.lang3.text.WordUtils;
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
        return new ItemBuilder(getIcon()).setDisplayName(
                ChatUtils.stringToComponentCC(
                        getParent().getCategory().getColor() +
                                ChatUtils.componentToString(ChatUtils.stringToComponent(getIcon().getItemMeta().displayName() == null ?
                                        WordUtils.capitalize(getIcon().getType().name().toLowerCase().replace("_", " ")) : ChatUtils.componentToString(getIcon().getItemMeta().displayName())))))
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
    }

    default ItemStack getItem() {
        return BazaarMiscUtil.getItem(getMaterial());
    }

}
