package me.lagggpixel.core.modules.chat;

import me.lagggpixel.core.modules.IModule;
import me.lagggpixel.core.modules.chat.commands.StaffChatCommand;
import me.lagggpixel.core.modules.chat.listeners.AsyncPlayerChatListener;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

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
