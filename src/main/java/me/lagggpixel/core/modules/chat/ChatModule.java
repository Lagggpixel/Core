/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.chat;

import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.chat.commands.StaffChatCommand;
import me.lagggpixel.core.modules.chat.listeners.AsyncPlayerChatListener;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class ChatModule implements IModule {
  @NotNull
  @Override
  public String getId() {
    return "chat";
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
    CommandUtils.registerCommand(new StaffChatCommand(this));
  }
  
  @Override
  public void registerListeners() {
    new AsyncPlayerChatListener();
  }
}
