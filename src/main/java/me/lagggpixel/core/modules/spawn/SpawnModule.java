/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

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
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
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
