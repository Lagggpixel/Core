package me.lagggpixel.core.modules.warp;

import me.lagggpixel.core.interfaces.IModule;
import org.jetbrains.annotations.NotNull;

public class WarpModule implements IModule {
  
  @Override
  public @NotNull String getId() {
    return "warps";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
  
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
  
  }
}
