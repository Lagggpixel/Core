package me.lagggpixel.core.modules.guilds;

import lombok.Getter;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.guilds.commands.GuildCommand;
import me.lagggpixel.core.modules.guilds.handlers.ClaimManager;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.modules.guilds.handlers.PillarManager;
import me.lagggpixel.core.modules.guilds.listeners.ClaimListeners;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

@Getter
public class GuildModule extends Module {
  private static GuildModule INSTANCE;
  private GuildHandler guildHandler;
  private ClaimManager claimManager;
  private PillarManager pillarManager;
  
  @NotNull
  @Override
  public String getId() {
    return "guilds";
  }


  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    INSTANCE = this;
    guildHandler = new GuildHandler();
    guildHandler.loadAllGuilds();
    guildHandler.startAutoSave();
    claimManager = new ClaimManager();
    pillarManager = new PillarManager();
  }
  
  @Override
  public void onDisable() {
    if (this.pillarManager != null) {
      this.pillarManager.removeAll();
    }
    guildHandler.stopAutoSave();
    guildHandler.saveAllGuilds();
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new GuildCommand(this));
  }

  @Override
  public void registerListeners() {
    new ClaimListeners();
  }
  
  public static GuildModule getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GuildModule();
    }
    return INSTANCE;
  }
  
}
