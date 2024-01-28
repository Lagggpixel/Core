/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skipnight.managers;

import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
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
