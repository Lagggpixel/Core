package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GamemodeHandler {
  
  public void setGameMode(Player player, GameMode gamemode) {
    User user = Main.getUser(player.getUniqueId());
    player.setGameMode(gamemode);
    user.sendMessage(Lang.STAFF_GAMEMODE_SELF.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase())));
  }
  
  public void setGameMode(Player sender, Player target, GameMode gamemode) {
    User senderUser = Main.getUser(sender.getUniqueId());
    User targetUser = Main.getUser(target.getUniqueId());
    target.setGameMode(gamemode);
    senderUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", target.getName())));
    targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", sender.getName())));
  }
  
  public void setGameMode(CommandSender commandSender, Player target, GameMode gamemode) {
    User targetUser = Main.getUser(target.getUniqueId());
    target.setGameMode(gamemode);
    commandSender.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", target.getName())));
    if (commandSender instanceof ConsoleCommandSender) {
      targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", "console")));
    }
    else {
      targetUser.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("%gamemode%", gamemode.name().toLowerCase(), "%player%", "unknown")));
    }
  }
  
}
