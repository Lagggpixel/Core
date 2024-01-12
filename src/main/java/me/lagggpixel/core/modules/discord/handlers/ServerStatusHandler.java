package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.DiscordModule;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatusHandler {
  
  private final long SERVER_PLAYERS_VC = 1151627010582523964L;
  
  public ServerStatusHandler() {
    updateAllChannelsTimer();
  }
  
  /**
   * Updates the timer for all channels on a 30 seconds time
   * <p>
   * Async method
   */
  private void updateAllChannelsTimer() {
    new BukkitRunnable() {
      @Override
      public void run() {
        updateServerPlayersVcChannel();
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20L * 30);
  }
  
  private void updateServerPlayersVcChannel() {
    boolean isWhiteListed = Main.getInstance().getServer().isWhitelistEnforced() || Main.getInstance().whitelisted;
    VoiceChannel voiceChannel = DiscordModule.discordHandler.getJda().getVoiceChannelById(SERVER_PLAYERS_VC);
    if (voiceChannel == null) {
      return;
    }
    if (isWhiteListed) {
      voiceChannel.getManager().setName("Server is whitelisted").queue();
    }
    else {
      AtomicInteger onlinePlayers = new AtomicInteger();
      Main.getUserData().forEach((uuid, userData) -> {
        if (userData.isOnline() && !userData.isVanished()) {
          onlinePlayers.getAndIncrement();
        }
      });
      voiceChannel.getManager().setName("Players: " + onlinePlayers.get()).queue();
    }
  }
}
