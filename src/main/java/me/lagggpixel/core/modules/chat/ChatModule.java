package me.lagggpixel.core.modules.chat;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.chat.commands.StaffChatCommand;
import me.lagggpixel.core.modules.chat.listeners.AsyncPlayerChatListener;
import me.lagggpixel.core.utils.CommandUtils;
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
  public void onEnable() {
  
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
