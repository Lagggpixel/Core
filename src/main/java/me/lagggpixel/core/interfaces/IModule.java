package me.lagggpixel.core.interfaces;

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
