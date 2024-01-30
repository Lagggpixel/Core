/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.skipnight.managers;

import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkipNightManager implements Runnable {

  private final World world;
  private final Plugin plugin;
  private final SkipNightVoteType skipNightVoteType;

  public SkipNightManager(World world, Plugin plugin, SkipNightVoteType skipNightVoteType) {
    this.world = world;
    this.plugin = plugin;
    this.skipNightVoteType = skipNightVoteType;
  }

  @Override
  public void run() {
    world.setTime(world.getTime() + 80);
    if (skipNightVoteType == SkipNightVoteType.NIGHT && world.getTime() > 12516 && world.getTime() < 23900)
      plugin.getServer().getScheduler().runTaskLater(plugin, this, 1);
  }
}
