package me.lagggpixel.core.modules.bazaar.interfaces;

import kotlin.Pair;
import lombok.Data;
import me.lagggpixel.core.modules.bazaar.impl.SkyblockBazaarCategory;
import me.lagggpixel.core.modules.bazaar.impl.SkyblockBazaarItem;
import me.lagggpixel.core.modules.bazaar.impl.SkyblockBazaarSubItem;
import me.lagggpixel.core.utils.MiscUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface BazaarConfigIndexer {

    @Data
    class BazaarConfigCategorySchema {
        private final String name;
        private final Material icon;
        private final ChatColor color;
        private final List<BazaarConfigCategoryItemSchema> items;

        public BazaarCategory toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
            List<BazaarItem> bazaarItems = new ArrayList<>();

            for (BazaarConfigCategoryItemSchema item : this.items) {
                bazaarItems.add(item.toBazaarEquivalent());
            }

            SkyblockBazaarCategory category = new SkyblockBazaarCategory(this.name, this.icon, this.color, MiscUtil.getPaneColor(this.color), bazaarItems);

            for (BazaarItem item : bazaarItems) {
                ((SkyblockBazaarItem) item).setCategory(category);
            }

            return category;
        }
    }

    @Data
    class BazaarConfigCategoryItemSchema {
        private final String name;
        private final int inventorySize;
        private final List<BazaarConfigCategorySubItemSchema> subItems;

        public BazaarItem toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
            List<BazaarSubItem> bazaarSubItems = new ArrayList<>();

            for (BazaarConfigCategorySubItemSchema subItem : this.subItems) {
                bazaarSubItems.add(subItem.toBazaarEquivalent());
            }

            BazaarItem item = new SkyblockBazaarItem(this.name, bazaarSubItems, this.inventorySize);

            for (BazaarSubItem subItem : bazaarSubItems) {
                ((SkyblockBazaarSubItem) subItem).setParent(item);
            }

            return item;
        }
    }

    @Data
    class BazaarConfigCategorySubItemSchema {
        private final String material;
        private final int slot;

        public BazaarSubItem toBazaarEquivalent() throws Bazaar.BazaarItemNotFoundException {
            ItemStack icon = MiscUtil.getItem(this.material);

            if (icon == null) {
                throw new Bazaar.BazaarItemNotFoundException("Could not find skyblock or minecraft material: " + this.material);
            }

            return new SkyblockBazaarSubItem(icon, this.slot, this.material ,new ArrayList<>(), new ArrayList<>());
        }
    }

    Pair<List<BazaarCategory>, List<BazaarSubItem>> index() throws Bazaar.BazaarIOException, Bazaar.BazaarItemNotFoundException;
    BazaarConfigCategorySchema indexCategory(Bazaar bazaar, JSONObject category);
    BazaarConfigCategoryItemSchema indexCategoryItem(Bazaar bazaar, JSONObject categoryItem);
    BazaarConfigCategorySubItemSchema indexCategorySubItem(Bazaar bazaar, JSONObject categorySubItem);

}
