package me.lagggpixel.core.modules.spawn;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.spawn.commands.SetSpawnCommand;
import me.lagggpixel.core.modules.spawn.commands.SpawnCommand;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class SpawnModule extends Module {
  
  private SpawnManager spawnManager;
  
  @Override
  public @NotNull String getId() {
    return "spawn";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
    spawnManager = new SpawnManager();
    spawnManager.loadSpawnLocation();
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new SpawnCommand(this, spawnManager));
    CommandUtils.registerCommand(new SetSpawnCommand(this, spawnManager));
  }
  
  @Override
  public void registerListeners() {
  
  }
}
