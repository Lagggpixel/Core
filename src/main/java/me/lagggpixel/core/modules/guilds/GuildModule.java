/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.guilds.commands.GuildCommand;
import me.lagggpixel.core.modules.guilds.data.loadsave.GuildLoadSave;
import me.lagggpixel.core.modules.guilds.handlers.ClaimManager;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.modules.guilds.handlers.PillarManager;
import me.lagggpixel.core.modules.guilds.hooks.placeholders.GuildExpansion;
import me.lagggpixel.core.modules.guilds.listeners.ClaimListeners;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@Getter
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class GuildModule implements IModule {
  private static GuildModule INSTANCE;
  private GuildHandler guildHandler;
  private ClaimManager claimManager;
  private PillarManager pillarManager;
  
  @NotNull
  @Override
  public String getId() {
    return "guilds";
  }


  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    INSTANCE = this;
    guildHandler = new GuildHandler();
    claimManager = new ClaimManager();
    pillarManager = new PillarManager();
    GuildLoadSave.load();
    guildHandler.startAutoSave();
    // Register guild placeholders
    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new GuildExpansion().register();
    }
  }
  
  @Override
  public void onDisable() {
    if (this.pillarManager != null) {
      this.pillarManager.removeAll();
    }
    guildHandler.stopAutoSave();
    GuildLoadSave.save();
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new GuildCommand(this));
  }

  @Override
  public void registerListeners() {
    new ClaimListeners();
  }
  
  public static GuildModule getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GuildModule();
    }
    return INSTANCE;
  }
  
}
