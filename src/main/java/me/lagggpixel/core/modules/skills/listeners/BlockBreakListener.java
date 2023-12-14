package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

  private final SkillsModule skillsModule;

  public BlockBreakListener(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void BlockBreakEvent(BlockBreakEvent event) {
    Block block = event.getBlock();
    // TODO: check if the block is naturally spawned

    if (skillsModule.getSkillHandler().isBlockFarming(block)) {
      // TODO: add farming handling
    }

    if (skillsModule.getSkillHandler().isBlockMining(block)) {
      // TODO: add mining handling
    }
  }

  private void handleFarmingBlockBreak() {

  }

  private void handleMiningBlockBreak() {

  }
}
