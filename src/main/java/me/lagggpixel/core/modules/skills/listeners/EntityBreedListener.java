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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

/**
 * @author Lagggpixel
 * @since February 15, 2024
 */
public class EntityBreedListener implements Listener {

  private final SkillsModule skillsModule;

  public EntityBreedListener(SkillsModule skillsModule) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.skillsModule = skillsModule;
  }

  @EventHandler(ignoreCancelled = true)
  public void EntityBreedEvent(EntityBreedEvent event) {

    LivingEntity livingEntity = event.getBreeder();
    if (!(livingEntity instanceof Player player)) {
      return;
    }

    User user = Main.getUser(player.getUniqueId());
    EntityType entityType = event.getFather().getType();

    if (skillsModule.getSkillHandler().isMobFarming(livingEntity)) {
      handleFarmingBreedMob(user, entityType);
    }

    if (skillsModule.getSkillHandler().isMobMining(livingEntity)) {
      handleMiningBreedMob(user, entityType);
    }

    if (skillsModule.getSkillHandler().isMobCombat(livingEntity)) {
      handleCombatBreedMob(user, entityType);
    }

    if (skillsModule.getSkillHandler().isMobWoodcutting(livingEntity)) {
      handleWoodcuttingBreedMob(user, entityType);
    }

  }

  private void handleFarmingBreedMob(User user, EntityType entityType) {
    double exp = skillsModule.getSkillHandler().getFarmingBreedMobs().get(entityType);
    user.getSkills().getFarming().addExp(exp, SkillExpGainCause.MOB_BREED);
  }

  private void handleMiningBreedMob(User user, EntityType entityType) {
    double exp = skillsModule.getSkillHandler().getMiningBreedMobs().get(entityType);
    user.getSkills().getMining().addExp(exp, SkillExpGainCause.MOB_BREED);
  }

  private void handleCombatBreedMob(User user, EntityType entityType) {
    double exp = skillsModule.getSkillHandler().getCombatBreedMobs().get(entityType);
    user.getSkills().getCombat().addExp(exp, SkillExpGainCause.MOB_BREED);
  }

  private void handleWoodcuttingBreedMob(User user, EntityType entityType) {
    double exp = skillsModule.getSkillHandler().getWoodcuttingBreedMobs().get(entityType);
    user.getSkills().getWoodcutting().addExp(exp, SkillExpGainCause.MOB_BREED);
  }
}
