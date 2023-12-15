package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

  private final SkillsModule skillsModule;

  public BlockPlaceListener(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void BlockPlaceEvent(BlockPlaceEvent event) {
    skillsModule.getSkillHandler().getNonNaturalBlocks().add(event.getBlock().getLocation());
  }
}
