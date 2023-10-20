package me.lagggpixel.core.modules.inventory;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.inventory.commands.EnderchestCommands;
import me.lagggpixel.core.modules.inventory.commands.InventoryCommands;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class InventoryModule extends Module {
  
  
  @Override
  public @NotNull String getId() {
    return "inventory";
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
    CommandUtils.registerCommand(new InventoryCommands());
    CommandUtils.registerCommand(new EnderchestCommands());
  }
  
  @Override
  public void registerListeners() {
  
  }
}
