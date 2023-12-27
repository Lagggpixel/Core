package me.lagggpixel.core.modules.discord.listener;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import me.lagggpixel.core.modules.economy.events.BalanceTopUpdateEvent;
import me.lagggpixel.core.utils.ChatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LoggingListeners implements Listener {

  private final TextChannel loggingChannel;

  public LoggingListeners() {
    this.loggingChannel = DiscordManager.getInstance().LOGGING_CHANNEL;

    Main.getPluginManager().registerEvents(this, Main.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void PlayerJoinEvent(@NotNull PlayerLoginEvent event) {
    if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
      return;
    }

    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.ORANGE);
    String message = "";
    if (event.getResult() == PlayerLoginEvent.Result.KICK_BANNED) {
      message = event.getPlayer().getName() + " failed to login as they are banned.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
      message = event.getPlayer().getName() + " failed to login as the server is full.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
      message = event.getPlayer().getName() + " failed to login as the server is whitelisted.";
    }
    else if (event.getResult() == PlayerLoginEvent.Result.KICK_OTHER) {
      message = event.getPlayer().getName() + " failed to login for an unknown reason.";
    }
    embedBuilder.setAuthor(message, null, DiscordManager.getInstance().getAvatarUrl(event.getPlayer()));
    embedBuilder.addField("Kick message", ChatUtils.componentToString(event.kickMessage()), false);
    embedBuilder.setFooter("AsyncPlayerPreLoginEvent result: " + event.getResult());
    DiscordManager.getInstance().sendEmbed(loggingChannel, embedBuilder.build());
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void BalanceTopUpdateEvent(@NotNull BalanceTopUpdateEvent event) {
    EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.ORANGE);
    embedBuilder.setAuthor("Balance top updated in " + event.getTimeTaken() + "ms", null, null);
    DiscordManager.getInstance().sendEmbed(loggingChannel, embedBuilder.build());
  }
  
}
