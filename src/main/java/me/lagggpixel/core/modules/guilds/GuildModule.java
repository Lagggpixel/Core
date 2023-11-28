package me.lagggpixel.core.modules.guilds;

import lombok.Getter;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.guilds.commands.GuildCommand;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

@Getter
public class GuildModule extends Module {
  private static GuildModule INSTANCE;
  private GuildHandler guildHandler;
  
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
  }
  
  @Override
  public void onDisable() {
    guildHandler.stopAutoSave();
    guildHandler.saveAllGuilds();
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new GuildCommand(this));
  }

  @Override
  public void registerListeners() {

  }
  
  public static GuildModule getInstance() {
    return INSTANCE;
  }
  
}
