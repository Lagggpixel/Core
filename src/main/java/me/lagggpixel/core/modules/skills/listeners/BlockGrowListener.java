package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener {

  private final SkillsModule skillsModule;

  public BlockGrowListener(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void BlockGrowEvent(BlockGrowEvent event) {
    skillsModule.getSkillHandler().getNonNaturalBlocks().remove(event.getBlock().getLocation());
  }
}
