package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.utils.MerchantModule;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.data.MerchantItem;
import me.lagggpixel.core.utils.NumberUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class MerchantItemSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantItemSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    User user = Main.getUser(sender);
    
    if (user.getCurrentMerchant() == null) {
      commandSender.sendMessage(Lang.MERCHANT_NONE_SELECTED.toComponentWithPrefix());
      return;
    }
    
    Merchant merchant = user.getCurrentMerchant();
    
    String subCmd = args[1];
    int slot;
    
    switch (subCmd) {
      case "set":
        // /merchant item set <price> <slot>
        if (args.length != 4) {
          commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          return;
        }
        int price;
        try {
          price = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
          sender.sendMessage(Lang.INVALID_NUMBER.toComponentWithPrefix());
          return;
        }
        try {
          slot = Integer.parseInt(args[3]);
        } catch (NumberFormatException exception) {
          sender.sendMessage(Lang.INVALID_NUMBER.toComponentWithPrefix());
          return;
        }
        if (slot > 28 || slot < 1) {
          sender.sendMessage(Lang.MERCHANT_ITEM_INVALID_SLOT.toComponentWithPrefix());
          return;
        }
        Material material = sender.getInventory().getItemInMainHand().getType();
        
        if (merchant.getItems().stream().map(MerchantItem::getRawSlot).toList().contains(slot)) {
          MerchantItem oldItem = null;
          for (MerchantItem item : merchant.getItems()) {
            if (item.getRawSlot() == slot) {
              oldItem = item;
              merchant.removeItem(item);
              break;
            }
          }
          assert oldItem != null;
          MerchantItem merchantItem = new MerchantItem(material, price, slot);
          merchant.addItem(merchantItem);
          sender.sendMessage(Lang.MERCHANT_ITEM_REPLACED.toComponentWithPrefix(Map.of(
              "%old%", oldItem.getMaterial().name(),
              "%new%", merchantItem.getMaterial().name(),
              "%old_price%", NumberUtil.formatInt(oldItem.getCost()),
              "%new_price%", NumberUtil.formatInt(merchantItem.getCost()),
              "%slot%", String.valueOf(oldItem.getRawSlot())
          )));
        } else {
          MerchantItem merchantItem = new MerchantItem(material, price, slot);
          merchant.addItem(merchantItem);
          sender.sendMessage(Lang.MERCHANT_ITEM_SET.toComponentWithPrefix(Map.of(
            "%material%", merchantItem.getMaterial().name(),
            "%price%", NumberUtil.formatInt(merchantItem.getCost()),
            "%slot%", String.valueOf(merchantItem.getRawSlot())
          )));
        }
        return;
      case "remove":
        // /merchant item remove <slot>
        if (args.length != 3) {
          commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
          return;
        }
        try {
          slot = Integer.parseInt(args[2]);
        } catch (NumberFormatException exception) {
          sender.sendMessage(Lang.INVALID_NUMBER.toComponentWithPrefix());
          return;
        }
        if (slot > 28 || slot < 1) {
          sender.sendMessage(Lang.MERCHANT_ITEM_INVALID_SLOT.toComponentWithPrefix());
          return;
        }
        if (!merchant.getItems().stream().map(MerchantItem::getRawSlot).toList().contains(slot)) {
          sender.sendMessage(Lang.MERCHANT_ITEM_NOT_FOUND.toComponentWithPrefix(Map.of(
              "%slot%", String.valueOf(slot)
          )));
          return;
        }
        for (MerchantItem item : merchant.getItems()) {
          if (item.getRawSlot() == slot) {
            merchant.removeItem(item);
            sender.sendMessage(Lang.MERCHANT_ITEM_REMOVED.toComponentWithPrefix(Map.of(
                "%material%", item.getMaterial().name(),
                "%price%", NumberUtil.formatInt(item.getCost()),
                "%slot%", String.valueOf(slot)
            )));
            break;
          }
        }
        return;
      default:
        commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        break;
    }
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    
    if (args.length == 2) {
      return List.of("set", "remove");
    }
    
    if (args.length == 3) {
      return List.of("cost");
    }
    
    if (args.length == 4) {
      return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
    }
    
    return List.of(" ");
  }
}
