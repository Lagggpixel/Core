package me.lagggpixel.core.modules.guilds;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class GuildModule extends Module {
  @NotNull
  @Override
  public String getId() {
    return "guilds";
  }


  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void onEnable() {

  }

  @Override
  public void registerCommands() {

  }

  @Override
  public void registerListeners() {

  }
}
