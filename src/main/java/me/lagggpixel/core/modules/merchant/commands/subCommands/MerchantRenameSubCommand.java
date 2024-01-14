package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MerchantRenameSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantRenameSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 2) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    User user = Main.getUser(sender);
    
    String name = args[1];
    
    if (user.getCurrentMerchant() == null) {
      sender.sendMessage(Lang.MERCHANT_NONE_SELECTED.toComponentWithPrefix());
      return;
    }
    
    user.getCurrentMerchant().setName(name);
  }
}
