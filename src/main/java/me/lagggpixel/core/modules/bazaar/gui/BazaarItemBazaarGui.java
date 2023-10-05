package me.lagggpixel.core.modules.bazaar.gui;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarCategory;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarItem;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class BazaarItemBazaarGui extends BazaarGui {

    public BazaarItemBazaarGui(Player player, BazaarCategory category, BazaarItem item) {
        super(category.getName().append(ChatUtils.stringToComponentCC(" ➜ " + item.getName())), item.getInventorySize(), new HashMap<>() {{
            for (BazaarSubItem subItem : item.getSubItems()) {
                put(subItem.getNamedIcon().toItemStack().getItemMeta().displayName(), () -> {
                    new BazaarSubItemBazaarGui(player, subItem).show(player);
                });
            }

            put(ChatUtils.stringToComponentCC("&aGo Back"), () ->
                    new BazaarCategoryBazaarGui(player, BazaarModule.getBazaar().getCategories().stream().filter(cat -> cat.getName().equals(category.getName().color(category.getColor()))).toList().get(0), false).show(player));
        }});

        BazaarMiscUtil.fillEmpty(this);

        for (BazaarSubItem subItem : item.getSubItems()) {
            ItemStack stack = new ItemStack(subItem.getIcon().getType());
            ItemMeta meta = stack.getItemMeta();

            meta.displayName(subItem.getIcon().getItemMeta().displayName() == null ? ChatUtils.stringToComponentCC(subItem.getIcon().getType().name()) : subItem.getIcon().getItemMeta().displayName());
            meta.lore(Arrays.asList(BazaarMiscUtil.buildLore(
                            "&7Buy Price: &6" + BazaarModule.getBazaar().getEscrow().getBuyPrice(subItem) +
                            "\n&7Sell Price: &6" + BazaarModule.getBazaar().getEscrow().getSellPrice(subItem) + "\n\n&eClick to view details!")));

            stack.setItemMeta(meta);

            this.addItem(subItem.getSlot(), stack);
        }

        this.addItem(31, BazaarMiscUtil.buildBackButton("&7To Bazaar"));
    }

}
