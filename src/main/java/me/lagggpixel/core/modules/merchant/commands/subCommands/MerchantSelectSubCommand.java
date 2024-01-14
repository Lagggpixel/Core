package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class MerchantSelectSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantSelectSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 2) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    String id = args[1];
    User user = Main.getUser(sender);
    
    if (id.equalsIgnoreCase("none")) {
      user.setCurrentMerchant(null);
      return;
    }
    
    if (!merchantModule.getMerchantHandler().hasMerchant(id)) {
      sender.sendMessage(Lang.MERCHANT_NOT_FOUND.toComponentWithPrefix(Map.of("merchant", id)));
      return;
    }
    
    user.setCurrentMerchant(id);
    sender.sendMessage(Lang.MERCHANT_SELECTED.toComponentWithPrefix(Map.of("merchant", id)));
  }
}
