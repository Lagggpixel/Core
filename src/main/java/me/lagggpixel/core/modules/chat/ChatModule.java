package me.lagggpixel.core.modules.chat;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.chat.listeners.AsyncPlayerChatListener;
import org.jetbrains.annotations.NotNull;

public class ChatModule extends Module {
  @NotNull
  @Override
  public String getId() {
    return "chat";
  }
  
  @Override
  public boolean isEnabled() {
    return false;
  }
  
  @Override
  public void initialize() {
  
  }
  
  @Override
  public void registerCommands() {
  
  }
  
  @Override
  public void registerListeners() {
    new AsyncPlayerChatListener();
  }
}
