package me.lagggpixel.core.modules.bazaar.gui;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class BazaarItemBazaarGui extends BazaarGui {

    public BazaarItemBazaarGui(Player player, BazaarCategory category, BazaarItem item) {
        super(category.getName() + " ➜ " + item.getName(), item.getInventorySize(), new HashMap<String, Runnable>() {{
            for (BazaarSubItem subItem : item.getSubItems()) {
                put(subItem.getNamedIcon().toItemStack().getItemMeta().getDisplayName(), () -> {
                    new BazaarSubItemBazaarGui(player, subItem).show(player);
                });
            }
        }});

        BazaarMiscUtil.fillEmpty(this);

        for (BazaarSubItem subItem : item.getSubItems()) {
            ItemStack stack = new ItemStack(subItem.getIcon().getType());
            ItemMeta meta = stack.getItemMeta();

            meta.setDisplayName(subItem.getIcon().getItemMeta().getDisplayName() == null ? subItem.getIcon().getType().name() : subItem.getIcon().getItemMeta().getDisplayName());
            meta.setLore(Arrays.asList(BazaarMiscUtil.buildLore(
                            "&7Buy Price: &6" + BazaarModule.getBazaar().getEscrow().getBuyPrice(subItem) +
                            "\n&7Sell Price: &6" + BazaarModule.getBazaar().getEscrow().getSellPrice(subItem) + "\n\n&eClick to view details!")));

            stack.setItemMeta(meta);

            this.addItem(subItem.getSlot(), stack);
        }

        this.addItem(31, BazaarMiscUtil.buildBackButton("&7To Bazaar"));
    }

}
