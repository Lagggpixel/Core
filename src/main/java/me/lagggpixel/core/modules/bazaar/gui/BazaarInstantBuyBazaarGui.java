package me.lagggpixel.core.modules.bazaar.gui;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.utils.ItemBuilder;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class BazaarInstantBuyBazaarGui extends BazaarGui {

    public BazaarInstantBuyBazaarGui(Player opener, BazaarSubItem item, int customAmount) {
        super(ChatColor.stripColor(item.getIcon().getItemMeta().getDisplayName()) + " ➜ Instant Buy", 36, new HashMap<>() {{
            put(ChatColor.GREEN + "Buy only " + ChatColor.YELLOW + "one" + ChatColor.GREEN + "!", () -> {

            });

            put(ChatColor.GREEN + "Buy a stack!", () -> {

            });

            put(ChatColor.GREEN + "Fill my inventory!", () -> {

            });

            put(ChatColor.GREEN + "Custom Amount", () -> {

            });

            put(ChatColor.GREEN + "Go Back", () -> new BazaarSubItemBazaarGui(opener, item).show(opener));
        }});

        BazaarMiscUtil.fillEmpty(this);

        String name = ChatColor.stripColor(item.getIcon().getItemMeta().getDisplayName());

        this.addItem(31, BazaarMiscUtil.buildBackButton("&7To " + name));

        Player player = opener;

        ItemStack icon = item.getIcon().clone();

        ItemStack skyblockItem = item.getItem();
        int possibleAmountToCarry = player.getInventory().firstEmpty() == -1 ? 0 : player.getInventory().firstEmpty() * skyblockItem.getMaxStackSize();

        ItemStack buyOne = new ItemBuilder(icon.clone()).setDisplayName(ChatColor.GREEN + "Buy only " + ChatColor.YELLOW + "one" + ChatColor.GREEN + "!").setLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "&8" + name + "\n\nAmount: &a1&7x\n\n" + (item.getLowestSellPrice() > 0.0 ? "Price: &6" + item.getLowestSellPrice() + " coins" : "&cNo sell offers!") +
                        (item.getLowestSellPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= 1 ?
                                (Main.getUser(player.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() ? "&cNot enough coins!" : "&eClick to buy now!")
                                : "&cNot enough inventory space!") : ""), '7'
        ))).toItemStack();

        ItemStack buyStack = new ItemBuilder(icon.clone()).setDisplayName(ChatColor.GREEN + "Buy a stack!").setLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "&8" + name + "\n\nAmount: &a" + skyblockItem.getMaxStackSize() + "&7x\n\n" + (item.getLowestSellPrice() > 0.0 ? "Per Unit: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice()) + " coins\n" + "Price: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice() * skyblockItem.getMaxStackSize()) + " coins" : "&cNo sell offers!") +
                        (item.getLowestSellPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= skyblockItem.getMaxStackSize() ?
                                (Main.getUser(player.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() * skyblockItem.getMaxStackSize() ? "&cNot enough coins!" : "&eClick to buy now!")
                                : "&cNot enough inventory space!") : ""), '7'
        ))).toItemStack();

        boolean canFillInventory = item.getLowestSellPrice(possibleAmountToCarry) > 0.0;

        ItemStack buyInventory = new ItemBuilder(ChatColor.GREEN + "Fill my inventory!", Material.CHEST).setLore(
                Arrays.asList(BazaarMiscUtil.buildLore(
                        "&8" + name + "\n\n" + (canFillInventory ? "Amount: &a" + BazaarMiscUtil.formatInt(possibleAmountToCarry) + "&7x\n\nPer Unit: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice()) + " coins\nPrice: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice() * possibleAmountToCarry) + " coins" : "No one is selling this item!") + "\n\n" +
                                (item.getLowestSellPrice() <= 0 ? "&cNo sell offers!" : (canFillInventory ? (Main.getUser(player.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() * possibleAmountToCarry ? "&cNot enough coins!" : "&eClick to buy now!") : "&cNot enough inventory space!"))
                        , '7'
                ))
        ).toItemStack();

        ItemStack customAmountItem;

        if (customAmount < 1) {
            customAmountItem = new ItemBuilder(ChatColor.GREEN + "Custom Amount", Material.OAK_SIGN).setLore(
                    Arrays.asList(BazaarMiscUtil.buildLore(
                            "&8Buy Order Quantity\n\nBuy up to &a71,680&7x.\n\n&eClick to specify!", '7'
                    ))
            ).toItemStack();
        } else {
            customAmountItem = new ItemBuilder(ChatColor.GREEN + "Custom Amount", Material.OAK_SIGN).setLore(
                    Arrays.asList(BazaarMiscUtil.buildLore(
                            "&8Buy Order Quantity\n\nYour Amount: &a" + customAmount + "&7x\n\n&bRight-Click to edit!\n&eClick to proceed!", '7'
                    ))
            ).toItemStack();
        }

        this.addItem(10, buyOne);
        this.addItem(12, buyStack);
        this.addItem(14, buyInventory);
        this.addItem(16, customAmountItem);
    }

}
