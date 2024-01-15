package me.lagggpixel.core.modules.merchant.commands.subCommands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MerchantListSubCommand implements ISubCommand {
  
  private final MerchantModule merchantModule;
  
  public MerchantListSubCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 1) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    sender.sendMessage(Lang.PREFIX.toComponent().append(ChatUtils.stringToComponentCC("Merchants:")));
    
    merchantModule.getMerchantHandler().getMerchants().forEach((merchantId, merchant) -> {
      sender.sendMessage(merchant.getId() + ": " + merchant.getName());
    });
  }
}
