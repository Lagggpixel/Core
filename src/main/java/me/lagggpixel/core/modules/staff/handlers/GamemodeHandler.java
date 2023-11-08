package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.data.Lang;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GamemodeHandler {
  
  public void setGameMode(Player player, GameMode gamemode) {
    player.setGameMode(gamemode);
    player.sendMessage(Lang.STAFF_GAMEMODE_SELF.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase())));
  }
  
  public void setGameMode(Player sender, Player target, GameMode gamemode) {
    target.setGameMode(gamemode);
    sender.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase(), "player", target.getName())));
    target.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase(), "player", sender.getName())));
  }
  
  public void setGameMode(CommandSender sender, Player target, GameMode gamemode) {
    target.setGameMode(gamemode);
    sender.sendMessage(Lang.STAFF_GAMEMODE_OTHER.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase(), "player", target.getName())));
    if (sender instanceof ConsoleCommandSender) {
      target.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase(), "player", "console")));
    }
    else {
      target.sendMessage(Lang.STAFF_GAMEMODE_OTHER_NOTIFY.toComponentWithPrefix(Map.of("gamemode", gamemode.name().toLowerCase(), "player", "unknown")));
    }
  }
  
}
