/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

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
