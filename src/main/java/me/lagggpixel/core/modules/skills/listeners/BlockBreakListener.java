package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
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
    
    if (skillsModule.getSkillHandler().getNonNaturalBlocks().contains(block.getLocation())) {
      return;
    }
    
    User user = Main.getUser(event.getPlayer().getUniqueId());
    if (skillsModule.getSkillHandler().isBlockFarming(block)) {
      handleFarmingBlockBreak(user, block);
    }

    if (skillsModule.getSkillHandler().isBlockMining(block)) {
      handleMiningBlockBreak(user, block);
    }

    if (skillsModule.getSkillHandler().isBlockCombat(block)) {
      handleCombatBlockBreak(user, block);
    }
  }

  private void handleFarmingBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getFarmingBlocks().get(block.getType());
    user.getSkills().getFarming().addExp(exp);
  }

  private void handleMiningBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getMiningBlocks().get(block.getType());
    user.getSkills().getMining().addExp(exp);
  }

  private void handleCombatBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getCombatBlocks().get(block.getType());
    user.getSkills().getCombat().addExp(exp);
  }
}
