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
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skills.enums.SkillExpGainCause;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class BlockBreakListener implements Listener {

  private final SkillsModule skillsModule;

  public BlockBreakListener(SkillsModule skillsModule) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.skillsModule = skillsModule;
  }

  @EventHandler(ignoreCancelled = true)
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

    if (skillsModule.getSkillHandler().isBlockWoodcutting(block)) {
      handleWoodcuttingBlockBreak(user, block);
    }

    if (skillsModule.getSkillHandler().isBlockFishing(block)) {
      handleFishingBlockBreak(user, block);
    }
  }

  private void handleFarmingBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getFarmingBlocks().get(block.getType());
    user.getSkills().getFarming().addExp(exp, SkillExpGainCause.BLOCK_BREAK);
  }

  private void handleMiningBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getMiningBlocks().get(block.getType());
    user.getSkills().getMining().addExp(exp, SkillExpGainCause.BLOCK_BREAK);
  }

  private void handleCombatBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getCombatBlocks().get(block.getType());
    user.getSkills().getCombat().addExp(exp, SkillExpGainCause.BLOCK_BREAK);
  }

  private void handleWoodcuttingBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getWoodcuttingBlocks().get(block.getType());
    user.getSkills().getWoodcutting().addExp(exp, SkillExpGainCause.BLOCK_BREAK);
  }

  private void handleFishingBlockBreak(User user, Block block) {
    double exp = skillsModule.getSkillHandler().getFishingBlocks().get(block.getType());
    user.getSkills().getFishing().addExp(exp, SkillExpGainCause.BLOCK_BREAK);
  }
}
