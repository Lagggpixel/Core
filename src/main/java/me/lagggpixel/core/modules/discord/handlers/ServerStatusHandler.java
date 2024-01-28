/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelUpdater;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class ServerStatusHandler {
  
  private final Optional<ServerVoiceChannel> serverPlayersVc;
  
  public ServerStatusHandler() {
    serverPlayersVc = DiscordHandler.getInstance().getDiscordApi()
        .getServerVoiceChannelById(DiscordHandler.getInstance().getYamlConfiguration().getString("playerCountChannelId"));
    
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
    if (serverPlayersVc.isEmpty()) {
      return;
    }
    boolean isWhiteListed = Main.getInstance().getServer().isWhitelistEnforced() || Main.getInstance().whitelisted;
    ServerVoiceChannelUpdater serverVoiceChannelUpdater = serverPlayersVc.get().createUpdater();
    if (serverVoiceChannelUpdater == null) {
      return;
    }
    if (isWhiteListed) {
      serverVoiceChannelUpdater.setName("Server is whitelisted").update();
    } else {
      AtomicInteger onlinePlayers = new AtomicInteger();
      Main.getUserData().forEach((uuid, userData) -> {
        if (userData.isOnline() && !userData.isVanished()) {
          onlinePlayers.getAndIncrement();
        }
      });
      serverVoiceChannelUpdater.setName("Players: " + onlinePlayers.get()).update();
    }
  }
  
  public void setServerPlayersVcChannelOffline() {
    if (serverPlayersVc.isEmpty()) {
      return;
    }
    ServerVoiceChannelUpdater serverVoiceChannelUpdater = serverPlayersVc.get().createUpdater();
    if (serverVoiceChannelUpdater == null) {
      return;
    }
    serverVoiceChannelUpdater.setName("Server is offline").update();
  }
}
