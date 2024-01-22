/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.warp;

import me.lagggpixel.core.interfaces.IModule;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
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
