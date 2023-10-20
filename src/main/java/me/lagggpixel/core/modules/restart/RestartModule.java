package me.lagggpixel.core.modules.restart;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class RestartModule extends Module {
  @NotNull
  @Override
  public String getId() {
    return "restart";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void initialize() {
  
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
  
  }
}
