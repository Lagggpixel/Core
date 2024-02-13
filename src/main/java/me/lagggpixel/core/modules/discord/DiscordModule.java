/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.discord.handlers.CaptureAppender;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import me.lagggpixel.core.modules.discord.handlers.ServerStatusHandler;
import me.lagggpixel.core.modules.discord.listener.Listeners;
import me.lagggpixel.core.modules.discord.listener.LoggingListeners;
import me.lagggpixel.core.modules.discord.handlers.NMSHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Getter
public class DiscordModule implements IModule {

  NMSHandler nmsHandler;
  public static DiscordHandler discordHandler;
  private ServerStatusHandler serverStatusHandler;

  @NotNull
  @Override
  public String getId() {
    return "discord";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    nmsHandler = new NMSHandler();
    discordHandler = new DiscordHandler(nmsHandler);
    serverStatusHandler = new ServerStatusHandler();
    // Log4j Appender implementation
    Main.getInstance().setLog4jLogger(LogManager.getRootLogger());
    boolean serverIsLog4jCapable = false;
    boolean serverIsLog4j21Capable = false;
    try {
      Class.forName("org.apache.logging.log4j.core.Logger");
      serverIsLog4jCapable = true;
    } catch (ClassNotFoundException e) {
      Main.log(Level.SEVERE, "Log4j classes are NOT available, console channel will not be attached");
    }
    try {
      Class.forName("org.apache.logging.log4j.core.Filter");
      serverIsLog4j21Capable = true;
    } catch (ClassNotFoundException e) {
      Main.log(Level.SEVERE, "Log4j 2.1 classes are NOT available, JDA messages will NOT be formatted properly");
    }

    if (serverIsLog4j21Capable && serverIsLog4jCapable) {
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.start();
      context.getRootLogger().addAppender(CaptureAppender.createAppender("CaptureAppender", false));
      CaptureAppender.clearCapturedLogs();
    }
  }

  @Override
  public void onDisable() {
    if (discordHandler.getYamlConfiguration().contains("stopMessage")) {
      discordHandler.MESSAGING_CHANNEL.sendMessage(discordHandler.getYamlConfiguration().getString("stopMessage")).join();
    }
    serverStatusHandler.setServerPlayersVcChannelOffline();
    DiscordModule.discordHandler.sendEmbed(DiscordModule.discordHandler.LOGGING_CHANNEL, new EmbedBuilder().setTitle("**Core Plugin Disabled**"));
  }

  @Override
  public void registerCommands() {

  }

  @Override
  public void registerListeners() {
    new Listeners(discordHandler);
    new LoggingListeners();
  }
}
