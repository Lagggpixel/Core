package me.lagggpixel.core.modules.warp;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class WarpModule extends Module {
  
  @Override
  public @NotNull String getId() {
    return "warps";
  }
  
  @Override
  public boolean isEnabled() {
    return false;
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
