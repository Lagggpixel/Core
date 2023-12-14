package me.lagggpixel.core.modules;

import javax.annotation.Nonnull;

public interface IModule {

  @Nonnull
  String getId();

  boolean isEnabled();

  void onEnable();
  
  void onDisable();

  void registerCommands();

  void registerListeners();
}
