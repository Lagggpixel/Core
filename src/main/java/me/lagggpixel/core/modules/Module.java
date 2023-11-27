package me.lagggpixel.core.modules;

import javax.annotation.Nonnull;

public abstract class Module {

  public @Nonnull abstract String getId();

  public abstract boolean isEnabled();

  public abstract void onEnable();
  
  public abstract void onDisable();

  public abstract void registerCommands();

  public abstract void registerListeners();
}
