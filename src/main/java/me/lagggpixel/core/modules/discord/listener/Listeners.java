package me.lagggpixel.core.modules.discord.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
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
  
  private final DiscordHandler discordHandler;
  
  public Listeners(DiscordHandler discordHandler) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.discordHandler = discordHandler;
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.join")) {
      discordHandler.sendEmbed(discordHandler.MESSAGING_CHANNEL, discordHandler.createJoinMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.leave")) {
      discordHandler.sendEmbed(discordHandler.MESSAGING_CHANNEL, discordHandler.createQuitMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void PlayerDeathEvent(@NotNull PlayerDeathEvent event) {
    if (!event.getPlayer().hasPermission("coreplugin.discord.silent.death")) {
      discordHandler.sendEmbed(discordHandler.MESSAGING_CHANNEL, discordHandler.createDeathMessageEmbed(event));
    }
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void AsyncChatEvent(@NotNull AsyncChatEvent event) {
    DiscordHandler.getInstance().sendEmbed(DiscordHandler.getInstance().MESSAGING_CHANNEL, DiscordHandler.getInstance().createChatEmbed(event));
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void GuildCreateEvent(@NotNull GuildCreateEvent event) {
    discordHandler.sendEmbed(discordHandler.LOGGING_CHANNEL, discordHandler.createGuildCreatedEmbed(event));
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void GuildCreateEvent(@NotNull GuildDisbandEvent event) {
    discordHandler.sendEmbed(discordHandler.LOGGING_CHANNEL, discordHandler.createGuildDisbandEmbed(event));
  }
}
