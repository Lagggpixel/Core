/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class NaturalBlocksChangedListeners implements Listener {

  private final SkillsModule skillsModule;

  public NaturalBlocksChangedListeners(SkillsModule skillsModule) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());

    this.skillsModule = skillsModule;
  }

  // A block is not "natural" after a player places a block there
  // A block will be considered natural if a creative player places it
  @EventHandler(ignoreCancelled = true)
  public void BlockPlaceEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.CREATIVE) {
      skillsModule.getSkillHandler().getNonNaturalBlocks().remove(event.getBlock().getLocation());
      return;
    }
    skillsModule.getSkillHandler().getNonNaturalBlocks().add(event.getBlock().getLocation());
  }

  // A block is "natural" after tnt/creeper/tnt-mine-cart explodes it
  @EventHandler(ignoreCancelled = true)
  public void EntityExplodeEvent(EntityExplodeEvent event) {
    for (Block block : event.blockList()) {
      Location location = block.getLocation();
      skillsModule.getSkillHandler().getNonNaturalBlocks().remove(location);
    }
  }

  // A block is "natural" after a crop grows there
  @EventHandler(ignoreCancelled = true)
  public void BlockGrowEvent(BlockGrowEvent event) {
    skillsModule.getSkillHandler().getNonNaturalBlocks().remove(event.getBlock().getLocation());
  }
}
