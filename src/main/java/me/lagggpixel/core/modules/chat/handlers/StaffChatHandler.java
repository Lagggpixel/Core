package me.lagggpixel.core.modules.chat.handlers;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class StaffChatHandler {
  
  public static void sendStaffChatMessage(Player sender, Component message) {
    Component messageToSend = Lang.CHAT_STAFF_CHAT.toComponent(Map.of(
        "%sender%", sender.getName(),
        "%message%", ChatUtils.componentToString(message)));
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission("coreplugin.chat.command.player.staffchat.view")) {
        player.sendMessage(messageToSend);
      }
    });
  }
  
}
