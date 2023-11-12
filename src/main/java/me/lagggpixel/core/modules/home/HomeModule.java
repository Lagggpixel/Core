package me.lagggpixel.core.modules.home;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.home.commands.HomeCommands;
import me.lagggpixel.core.modules.home.listeners.HomeGuiListeners;
import me.lagggpixel.core.modules.home.handlers.HomeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class HomeModule extends Module {
  
  private HomeHandler homeHandler;
  
  @Override
  public @NotNull String getId() {
    return "home";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
    homeHandler = new HomeHandler();
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new HomeCommands(this, homeHandler));
  }
  
  @Override
  public void registerListeners() {
    new HomeGuiListeners(homeHandler);
  }
}
