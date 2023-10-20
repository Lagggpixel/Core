package me.lagggpixel.core.modules.rtp;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.rtp.commands.RtpCommand;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class RtpModule extends Module {
  
  @Override
  public @NotNull String getId() {
    return "rtp";
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
    CommandUtils.registerCommand(new RtpCommand());
  }
  
  @Override
  public void registerListeners() {
  
  }
}
