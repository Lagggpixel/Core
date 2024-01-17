/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.home;

import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.home.commands.HomeCommands;
import me.lagggpixel.core.modules.home.listeners.HomeGuiListeners;
import me.lagggpixel.core.modules.home.handlers.HomeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class HomeModule implements IModule {
  
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
  public void onDisable() {
  
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
