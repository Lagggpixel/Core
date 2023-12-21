package me.lagggpixel.core.modules.discord;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.discord.handlers.CaptureAppender;
import me.lagggpixel.core.modules.discord.listener.Listeners;
import me.lagggpixel.core.modules.discord.listener.LoggingListeners;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import me.lagggpixel.core.modules.discord.managers.NMSManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class DiscordModule implements IModule {
  
  NMSManager nmsManager;
  public static DiscordManager discordManager;
  
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
    nmsManager = new NMSManager();
    discordManager = new DiscordManager(nmsManager);

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
      serverIsLog4j21Capable = Class.forName("org.apache.logging.log4j.core.Filter") != null;
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
  
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
    new Listeners(discordManager);
    new LoggingListeners();
  }
}
