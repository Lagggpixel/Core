package me.lagggpixel.core.modules.spawn;

import lombok.Getter;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.spawn.commands.SetSpawnCommand;
import me.lagggpixel.core.modules.spawn.commands.SpawnCommand;
import me.lagggpixel.core.modules.spawn.listeners.PlayerJoinListener;
import me.lagggpixel.core.modules.spawn.listeners.PlayerMoveListener;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

@Getter
public class SpawnModule implements IModule {

  @Getter
  private static SpawnModule instance;
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
    instance = this;
    spawnManager = new SpawnManager();
    spawnManager.loadSpawnLocation();
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new SpawnCommand(this));
    CommandUtils.registerCommand(new SetSpawnCommand(this));
  }
  
  @Override
  public void registerListeners() {
   new PlayerJoinListener(this);
   new PlayerMoveListener(this);
  }
  
}
