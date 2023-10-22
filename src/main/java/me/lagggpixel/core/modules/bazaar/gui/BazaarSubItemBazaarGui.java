package me.lagggpixel.core.modules.bazaar.gui;

import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.escrow.EscrowTransaction;
import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.modules.bazaar.utils.BazaarMiscUtil;
import me.lagggpixel.core.modules.bazaar.utils.gui.BazaarGui;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BazaarSubItemBazaarGui extends BazaarGui {

    public BazaarSubItemBazaarGui(Player player, BazaarSubItem item) {
        super(item.getParent().getCategory().getName().append(ChatUtils.stringToComponentCC(" ➜ ")).append(ChatUtils.stripColor(item.getNamedIcon().toItemStack().getItemMeta().displayName())), 36, new HashMap<>() {{
            Runnable back = () -> new BazaarCategoryBazaarGui(player, item.getParent().getCategory(), false).show(player);

            put(ChatUtils.stringToComponentCC("&aGo Back"), back);
            put(ChatUtils.stringToComponentCC("&6Go Back"), back);
            put(ChatUtils.stringToComponentCC("&aManage Orders"), () -> {
                // TODO: implement manage orders
            });
            put(ChatUtils.stringToComponentCC("&aView Graphs"), () -> {
                // TODO: implement graphs
            });

            put(ChatUtils.stringToComponentCC("&aBuy Instantly"), () -> new BazaarInstantBuyBazaarGui(player, item, 0).show(player));
        }});

        BazaarMiscUtil.fillEmpty(this);

        AtomicInteger amountToSell = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).map(stack -> new ItemBuilder(stack.clone()).setDisplayName(ChatUtils.stringToComponent(ChatUtils.componentToString(stack.getItemMeta().displayName()))).toItemStack()).filter(stack -> stack.isSimilar(new ItemBuilder(item.getItem().clone()).setDisplayName(ChatUtils.stringToComponent(ChatUtils.componentToString(item.getItem().getItemMeta().displayName()))).toItemStack())).forEach(stack -> amountToSell.addAndGet(stack.getAmount()));

        List<EscrowTransaction> top6SellOrders = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).limit(6).toList();
        List<EscrowTransaction> top6BuyOrders = BazaarModule.getBazaar().getEscrow().getRankedBuyOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).limit(6).toList();

        this.addItem(10, new ItemBuilder(ChatUtils.stringToComponentCC("&aBuy Instantly"), Material.GOLDEN_HORSE_ARMOR).addLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\n" + "&7Price per unit: &6" + BazaarModule.getBazaar().getEscrow().getBuyPrice(item) + "\n" +
                        "&7Stack price: &6" + BazaarModule.getBazaar().getEscrow().getBuyPrice(item) * 64 + "\n\n" +
                        "&eClick to pick amount!"
        ))).toItemStack());

        this.addItem(11, new ItemBuilder(ChatUtils.stringToComponentCC("&6Sell Instantly"), Material.HOPPER).addLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\n" + "&7Inventory: &a" + amountToSell.get() + "\n\n" +
                        "&7Amount: &a" + amountToSell.get() + "&7x\n" +
                        "&7Total: &6" + BazaarModule.getBazaar().getEscrow().getSellPrice(item) * amountToSell.get() + " coins\n" +
                        "&8Current tax: " + Bazaar.BAZAAR_TAX + "%\n\n" +
                        "&bRight-Click to pick amount!\n&eClick to sell inventory!"
        ))).toItemStack());

        this.addItem(13, item.getItem());

        this.addItem(15, new ItemBuilder(ChatUtils.stringToComponentCC("&aCreate Buy Order"), Material.MAP).addLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\n" + "&aTop Orders:\n" + (top6BuyOrders.isEmpty() ? "&c No buy orders!" : top6BuyOrders.stream().map(transaction -> "&8- &6" + transaction.getPrice() + " coins&7 each | &a" + transaction.getAmount() + "&7x from &f1 &7offer").collect(Collectors.joining("\n"))) +
                        (top6BuyOrders.isEmpty() ? "" : "\n&eClick to setup buy order!")
        ))).toItemStack());

        this.addItem(16, new ItemBuilder(ChatUtils.stringToComponentCC("&6Create Sell Offer"), Material.MAP).addLore(Arrays.asList(BazaarMiscUtil.buildLore(
                "\n" +
                        "&6Top Offers:\n" + (top6SellOrders.isEmpty() ? "&c No sell orders!" : top6SellOrders.stream().map(transaction -> "&8- &6" + transaction.getPrice() + " coins&7 each | &a" + transaction.getAmount() + "&7x from &f1 &7offer").collect(Collectors.joining("\n"))) +
                        (top6SellOrders.isEmpty() ? "" : (amountToSell.get() < 1 ? "\n&8None in inventory!" : "\n&eClick to setup sell order!"))
        ))).toItemStack());
    }

}
