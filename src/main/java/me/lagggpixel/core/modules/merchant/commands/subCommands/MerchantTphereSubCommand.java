package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.utils.MerchantModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MerchantTphereSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantTphereSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 1) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    User user = Main.getUser(sender);
    if (user.getCurrentMerchant() == null) {
      commandSender.sendMessage(Lang.MERCHANT_NONE_SELECTED.toComponentWithPrefix());
      return;
    }
    
    user.getCurrentMerchant().setLocation(sender.getLocation());
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
