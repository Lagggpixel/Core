package me.lagggpixel.core.modules.chatgames;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class ChatgamesModule extends Module {
  @NotNull
  @Override
  public String getId() {
    return "chatgames";
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
  
  }
  
  @Override
  public void registerListeners() {
  
  }
}
