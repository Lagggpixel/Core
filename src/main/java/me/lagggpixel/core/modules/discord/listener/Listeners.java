package me.lagggpixel.core.modules.discord.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
  
  private final DiscordManager discordManager;
  
  public Listeners(DiscordManager discordManager) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.discordManager = discordManager;
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
    if (event.getPlayer().hasPermission("coreplugin.discord.silent.join")) {
      discordManager.sendEmbed(discordManager.MESSAGING_CHANNEL, discordManager.createJoinMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
    if (event.getPlayer().hasPermission("coreplugin.discord.silent.leave")) {
      discordManager.sendEmbed(discordManager.MESSAGING_CHANNEL, discordManager.createQuitMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerDeathEvent(@NotNull PlayerDeathEvent event) {
    if (event.getPlayer().hasPermission("coreplugin.discord.silent.death")) {
      discordManager.sendEmbed(discordManager.MESSAGING_CHANNEL, discordManager.createDeathMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void AsyncChatEvent(@NotNull AsyncChatEvent event) {
    DiscordManager.getInstance().sendEmbed(DiscordManager.getInstance().MESSAGING_CHANNEL, DiscordManager.getInstance().createChatEmbed(event));
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void GuildCreateEvent(@NotNull GuildCreateEvent event) {
    discordManager.sendEmbed(discordManager.LOGGING_CHANNEL, discordManager.createGuildCreatedEmbed(event));
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void GuildCreateEvent(@NotNull GuildDisbandEvent event) {
    discordManager.sendEmbed(discordManager.LOGGING_CHANNEL, discordManager.createGuildDisbandEmbed(event));
  }
}
