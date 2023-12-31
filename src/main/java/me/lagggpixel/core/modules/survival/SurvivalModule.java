package me.lagggpixel.core.modules.survival;

import me.lagggpixel.core.interfaces.IModule;
import org.jetbrains.annotations.NotNull;

public class SurvivalModule implements IModule {
  @NotNull
  @Override
  public String getId() {
    return "survival";
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
