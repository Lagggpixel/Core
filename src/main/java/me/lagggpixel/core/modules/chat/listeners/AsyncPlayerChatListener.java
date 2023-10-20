package me.lagggpixel.core.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {
  
  public AsyncPlayerChatListener() {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @EventHandler
  public void AsyncPlayerChatEvent(AsyncChatEvent event) {
    DiscordManager.getInstance().sendEmbed(DiscordManager.getInstance().MESSAGING_CHANNEL, DiscordManager.getInstance().createChatEmbed(event));
  }
  
}
