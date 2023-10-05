package me.lagggpixel.core.modules.bazaar.gui;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.escrow.EscrowTransaction;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BazaarInstantBuyBazaarGui extends BazaarGui {

    public BazaarInstantBuyBazaarGui(Player opener, BazaarSubItem item, int customAmount) {
        super(Objects.requireNonNull(item.getIcon().getItemMeta().displayName()).append(ChatUtils.stringToComponentCC(" ➜ Instant Buy")), 36, new HashMap<>() {{
            put(ChatUtils.stringToComponentCC("&aBuy only &eone&a!"), () -> {
                int amountToBuy = 1;
                if (item.getLowestSellPrice() < 0.0) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
                    return;
                }
                if (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice(amountToBuy)) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough coins!"));
                    return;
                }
                int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();

                if (possibleAmountToCarry < amountToBuy) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough inventory space!"));
                    return;
                }

                while (amountToBuy > 0) {
                    EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
                    int amountBuyable = escrowTransaction.getAmount();
                    if (amountBuyable > amountToBuy) {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToBuy);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountToBuy);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                    else {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountBuyable);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountBuyable);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                }
            });

            put(ChatUtils.stringToComponentCC("&aBuy a stack!"), () -> {
                int amountToBuy = item.getItem().getMaxStackSize();
                if (item.getLowestSellPrice() < 0.0) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
                    return;
                }
                AtomicInteger possibleToBuy = new AtomicInteger();
                item.getOffers().forEach(offer -> {
                    possibleToBuy.set(possibleToBuy.get() + offer.getAmount());
                });
                if (amountToBuy > possibleToBuy.get()) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items in escrow!"));
                    return;
                }
                if (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice(amountToBuy)) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough coins!"));
                    return;
                }
                int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();

                if (possibleAmountToCarry < amountToBuy) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough inventory space!"));
                    return;
                }

                while (amountToBuy > 0) {
                    EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
                    int amountBuyable = escrowTransaction.getAmount();
                    if (amountBuyable > amountToBuy) {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToBuy);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountToBuy);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                    else {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountBuyable);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountBuyable);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                }
            });

            put(ChatUtils.stringToComponentCC("&aFill my inventory!"), () -> {
                int amountToBuy = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();
                if (item.getLowestSellPrice() < 0.0) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
                    return;
                }
                AtomicInteger possibleToBuy = new AtomicInteger();
                item.getOffers().forEach(offer -> {
                    possibleToBuy.set(possibleToBuy.get() + offer.getAmount());
                });
                if (amountToBuy > possibleToBuy.get()) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items in escrow!"));
                    return;
                }
                if (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice(amountToBuy)) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough coins!"));
                    return;
                }
                int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();

                if (possibleAmountToCarry < amountToBuy) {
                    opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough inventory space!"));
                    return;
                }

                while (amountToBuy > 0) {
                    EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
                    int amountBuyable = escrowTransaction.getAmount();
                    if (amountBuyable > amountToBuy) {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToBuy);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountToBuy);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                    else {
                        BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountBuyable);
                        ItemStack itemstack = item.getItem();
                        itemstack.setAmount(amountBuyable);
                        opener.getInventory().addItem(itemstack);
                        opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToBuy + "&6 "
                                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
                        amountToBuy = amountToBuy - amountBuyable;
                    }
                }
            });

            put(ChatUtils.stringToComponentCC("&aCustom Amount"), () -> {

                // TODO: implement custom amount logic

                opener.sendMessage(ChatUtils.stringToComponentCC("&cYou can not use this yet!"));
            });

            put(ChatUtils.stringToComponentCC("&aGo Back"), () -> new BazaarSubItemBazaarGui(opener, item).show(opener));
        }});

        BazaarMiscUtil.fillEmpty(this);

        this.addItem(31, BazaarMiscUtil.buildBackButton("&7To " + ChatUtils.componentToString(item.getItem().displayName())));

        ItemStack icon = item.getIcon().clone();

        ItemStack bazaarItem = item.getItem();
        int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * bazaarItem.getMaxStackSize();

        ItemStack buyOne = new ItemBuilder(icon.clone()).setDisplayName(ChatUtils.stringToComponentCC("&aBuy only &eone&a!")).setLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\nAmount: &a1&7x\n\n" + (item.getLowestSellPrice() > 0.0 ? "Price: &6" + item.getLowestSellPrice() + " coins" : "&cNo sell offers!") +
                        (item.getLowestSellPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= 1 ?
                                (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() ? "&cNot enough coins!" : "&eClick to buy now!")
                                : "&cNot enough inventory space!") : ""), '7'
        ))).toItemStack();

        ItemStack buyStack = new ItemBuilder(icon.clone()).setDisplayName(ChatUtils.stringToComponentCC("&aBuy a stack!")).setLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\nAmount: &a" + bazaarItem.getMaxStackSize() + "&7x\n\n" + (item.getLowestSellPrice() > 0.0 ? "Per Unit: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice()) + " coins\n" + "Price: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice() * bazaarItem.getMaxStackSize()) + " coins" : "&cNo sell offers!") +
                        (item.getLowestSellPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= bazaarItem.getMaxStackSize() ?
                                (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() * bazaarItem.getMaxStackSize() ? "&cNot enough coins!" : "&eClick to buy now!")
                                : "&cNot enough inventory space!") : ""), '7'
        ))).setAmount(64).toItemStack();

        boolean canFillInventory = item.getLowestSellPrice(possibleAmountToCarry) > 0.0;

        ItemStack buyInventory = new ItemBuilder(ChatUtils.stringToComponentCC("&aFill my inventory!"), Material.CHEST).setLore(
                Arrays.asList(BazaarMiscUtil.buildLore(
                        "\n" + (canFillInventory ? "Amount: &a" + BazaarMiscUtil.formatInt(possibleAmountToCarry) + "&7x\n\nPer Unit: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice()) + " coins\nPrice: &6" + BazaarMiscUtil.formatDouble(item.getLowestSellPrice() * possibleAmountToCarry) + " coins" : "No one is selling this item!") + "\n\n" +
                                (item.getLowestSellPrice() <= 0 ? "&cNo sell offers!" : (canFillInventory ? (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getLowestSellPrice() * possibleAmountToCarry ? "&cNot enough coins!" : "&eClick to buy now!") : "&cNot enough inventory space!"))
                        , '7'
                ))
        ).toItemStack();

        ItemStack customAmountItem;

        if (customAmount < 1) {
            customAmountItem = new ItemBuilder(ChatUtils.stringToComponentCC("&aCustom Amount"), Material.OAK_SIGN).setLore(
                    Arrays.asList(BazaarMiscUtil.buildLore(
                            "&8Buy Order Quantity\n\nBuy up to &a71,680&7x.\n\n&eClick to specify!", '7'
                    ))
            ).toItemStack();
        } else {
            customAmountItem = new ItemBuilder(ChatUtils.stringToComponentCC("&aCustom Amount"), Material.OAK_SIGN).setLore(
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
