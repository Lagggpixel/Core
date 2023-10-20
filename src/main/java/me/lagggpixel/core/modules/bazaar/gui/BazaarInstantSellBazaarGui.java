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

public class BazaarInstantSellBazaarGui extends BazaarGui {
  
  public BazaarInstantSellBazaarGui(Player opener, BazaarSubItem item, int customAmount) {
    super(Objects.requireNonNull(item.getIcon().getItemMeta().displayName()).append(ChatUtils.stringToComponentCC(" ➜ Instant Sell")), 36, new HashMap<>() {{
      put(ChatUtils.stringToComponentCC("&aSell only &eone&a!"), () -> {
        int amountToSell = 1;
        if (item.getHighestBuyPrice() < 0.0) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
          return;
        }
        if (opener.getInventory().containsAtLeast(item.getItem(), amountToSell)) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items in your inventory to sell!"));
          return;
        }
        
        while (amountToSell > 0) {
          EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedBuyOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
          int amountSellable = escrowTransaction.getAmount();
          if (amountSellable > amountToSell) {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToSell);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountToSell);
            opener.getInventory().removeItemAnySlot(itemstack);
          } else {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountSellable);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountSellable);
            opener.getInventory().removeItemAnySlot(itemstack);
          }
          opener.sendMessage(ChatUtils.stringToComponentCC("&6You have sold &e" + amountToSell + "&6 "
              + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
          amountToSell = amountToSell - amountSellable;
        }
      });
      
      put(ChatUtils.stringToComponentCC("&aSell a stack!"), () -> {
        int amountToSell = item.getItem().getMaxStackSize();
        if (item.getHighestBuyPrice() < 0.0) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
          return;
        }
        AtomicInteger possibleToSell = new AtomicInteger();
        item.getOffers().forEach(offer -> {
          possibleToSell.set(possibleToSell.get() + offer.getAmount());
        });
        if (amountToSell > possibleToSell.get()) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough buy orders in escrow!"));
          return;
        }
        if (opener.getInventory().containsAtLeast(item.getItem(), amountToSell)) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items in your inventory to sell!"));
          return;
        }
        int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();
        
        if (possibleAmountToCarry < amountToSell) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough inventory space!"));
          return;
        }
        
        while (amountToSell > 0) {
          EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
          int amountSellable = escrowTransaction.getAmount();
          if (amountSellable > amountToSell) {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToSell);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountToSell);
            opener.getInventory().addItem(itemstack);
            opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToSell + "&6 "
                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
            amountToSell = amountToSell - amountSellable;
          } else {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountSellable);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountSellable);
            opener.getInventory().addItem(itemstack);
            opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToSell + "&6 "
                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
            amountToSell = amountToSell - amountSellable;
          }
        }
      });
      
      put(ChatUtils.stringToComponentCC("&aSell all!"), () -> {
        int amountToSell = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();
        if (item.getLowestSellPrice() < 0.0) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
          return;
        }
        AtomicInteger possibleToSell = new AtomicInteger();
        item.getOffers().forEach(offer -> {
          possibleToSell.set(possibleToSell.get() + offer.getAmount());
        });
        if (amountToSell > possibleToSell.get()) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items in your inventory to sell!"));
          return;
        }
        if (opener.getInventory().containsAtLeast(item.getItem(), amountToSell)) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough items!"));
          return;
        }
        int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * item.getItem().getMaxStackSize();
        
        if (possibleAmountToCarry < amountToSell) {
          opener.sendMessage(ChatUtils.stringToComponentCC("&cNot enough inventory space!"));
          return;
        }
        
        while (amountToSell > 0) {
          EscrowTransaction escrowTransaction = BazaarModule.getBazaar().getEscrow().getRankedSellOrders().stream().filter(transaction -> transaction.getSubItem().equals(item)).findFirst().get();
          int amountSellable = escrowTransaction.getAmount();
          if (amountSellable > amountToSell) {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountToSell);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountToSell);
            opener.getInventory().addItem(itemstack);
            opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToSell + "&6 "
                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
            amountToSell = amountToSell - amountSellable;
          } else {
            BazaarModule.getBazaar().getEscrow().fillSellOrder(escrowTransaction, amountSellable);
            ItemStack itemstack = item.getItem();
            itemstack.setAmount(amountSellable);
            opener.getInventory().addItem(itemstack);
            opener.sendMessage(ChatUtils.stringToComponentCC("&6You have bought &e" + amountToSell + "&6 "
                + ChatUtils.componentToString(item.getItem().displayName()) + " for &e" + escrowTransaction.getPrice() + "each&6!"));
            amountToSell = amountToSell - amountSellable;
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
    
    String name = ChatUtils.componentToString(Objects.requireNonNull(item.getIcon().getItemMeta().displayName()));
    
    this.addItem(31, BazaarMiscUtil.buildBackButton("&7To " + name));
    
    ItemStack icon = item.getIcon().clone();
    
    ItemStack bazaarItem = item.getItem();
    int possibleAmountToCarry = opener.getInventory().firstEmpty() == -1 ? 0 : opener.getInventory().firstEmpty() * bazaarItem.getMaxStackSize();
    
    ItemStack sellOne = new ItemBuilder(icon.clone()).setDisplayName(ChatUtils.stringToComponentCC("&aSell only &eone&a!")).setLore(Arrays.asList(BazaarMiscUtil.buildLore(
        "&8" + name + "\n\nAmount: &a1&7x\n\n" + (item.getHighestBuyPrice() > 0.0 ? "Price: &6" + item.getHighestBuyPrice() + " coins" : "&cNo buy offers!") +
            (item.getHighestBuyPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= 1 ?
                (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getHighestBuyPrice() ? "&cNot enough coins!" : "&eClick to sell now!")
                : "&cNot enough inventory space!") : ""), '7'
    ))).toItemStack();
    
    ItemStack sellStack = new ItemBuilder(icon.clone()).setDisplayName(ChatUtils.stringToComponentCC("&aSell a stack!")).setLore(Arrays.asList(BazaarMiscUtil.buildLore(
        "&8" + name + "\n\nAmount: &a" + bazaarItem.getMaxStackSize() + "&7x\n\n" + (item.getHighestBuyPrice() > 0.0 ? "Per Unit: &6" + BazaarMiscUtil.formatDouble(item.getHighestBuyPrice()) + " coins\n" + "Price: &6" + BazaarMiscUtil.formatDouble(item.getHighestBuyPrice() * bazaarItem.getMaxStackSize()) + " coins" : "&cNo sell offers!") +
            (item.getHighestBuyPrice() > 0.0 ? "\n\n" + (possibleAmountToCarry >= bazaarItem.getMaxStackSize() ?
                (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getHighestBuyPrice() * bazaarItem.getMaxStackSize() ? "&cNot enough coins!" : "&eClick to sell now!")
                : "&cNot enough inventory space!") : ""), '7'
    ))).setAmount(64).toItemStack();
    
    boolean canFillInventory = item.getHighestBuyPrice(possibleAmountToCarry) > 0.0;
    
    ItemStack sellAll = new ItemBuilder(ChatUtils.stringToComponentCC("&aSell all!"), Material.CHEST).setLore(
        Arrays.asList(BazaarMiscUtil.buildLore(
            "&8" + name + "\n\n" + (canFillInventory ? "Amount: &a" + BazaarMiscUtil.formatInt(possibleAmountToCarry) + "&7x\n\nPer Unit: &6" + BazaarMiscUtil.formatDouble(item.getHighestBuyPrice()) + " coins\nPrice: &6" + BazaarMiscUtil.formatDouble(item.getHighestBuyPrice() * possibleAmountToCarry) + " coins" : "No one is selling this item!") + "\n\n" +
                (item.getHighestBuyPrice() <= 0 ? "&cNo sell offers!" : (canFillInventory ? (Main.getUser(opener.getUniqueId()).getPlayerBalance() < item.getHighestBuyPrice() * possibleAmountToCarry ? "&cNot enough coins!" : "&eClick to sell now!") : "&cNot enough inventory space!"))
            , '7'
        ))
    ).toItemStack();
    
    ItemStack customAmountItem;
    
    if (customAmount < 1) {
      customAmountItem = new ItemBuilder(ChatUtils.stringToComponentCC("&aCustom Amount"), Material.OAK_SIGN).setLore(
          Arrays.asList(BazaarMiscUtil.buildLore(
              "&8Sell Order Quantity\n\nSell up to &a71,680&7x.\n\n&eClick to specify!", '7'
          ))
      ).toItemStack();
    } else {
      customAmountItem = new ItemBuilder(ChatUtils.stringToComponentCC("&aCustom Amount"), Material.OAK_SIGN).setLore(
          Arrays.asList(BazaarMiscUtil.buildLore(
              "&8Sell Order Quantity\n\nYour Amount: &a" + customAmount + "&7x\n\n&bRight-Click to edit!\n&eClick to proceed!", '7'
          ))
      ).toItemStack();
    }
    
    this.addItem(10, sellOne);
    this.addItem(12, sellStack);
    this.addItem(14, sellAll);
    this.addItem(16, customAmountItem);
  }
  
}
