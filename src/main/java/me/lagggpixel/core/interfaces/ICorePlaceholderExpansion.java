/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.interfaces;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lagggpixel.core.Main;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public abstract class ICorePlaceholderExpansion extends PlaceholderExpansion {
  
  protected String version = Main.getInstance().getPluginMeta().getVersion();
  
  @Override
  public @NotNull String getIdentifier() {
    return "core";
  }

  @Override
  public @NotNull String getAuthor() {
    return "Lagggpixel";
  }

  @Override
  public @NotNull String getVersion() {
    return version;
  }

  @Override
  public abstract String onRequest(OfflinePlayer player, @NotNull String params);
}
