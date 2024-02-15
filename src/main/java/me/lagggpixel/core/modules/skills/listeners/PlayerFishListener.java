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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * @author Lagggpixel
 * @since February 15, 2024
 */
public class PlayerFishListener implements Listener {

  private final SkillsModule skillsModule;

  public PlayerFishListener(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerFish(PlayerFishEvent event) {

    Player player = event.getPlayer();
    User user = Main.getUser(player.getUniqueId());

    Entity entity = event.getCaught();

    if (entity == null) {
      return;
    }

    Item item = (Item) entity;

    if (skillsModule.getSkillHandler().isFishFarming(item.getType())) {
      handleFarmingFishing(user, item);
    }

    if (skillsModule.getSkillHandler().isFishMining(item.getType())) {
      handleMiningFishing(user, item);
    }

    if (skillsModule.getSkillHandler().isFishCombat(item.getType())) {
      handleCombatFishing(user, item);
    }

    if (skillsModule.getSkillHandler().isFishWoodcutting(item.getType())) {
      handleWoodcuttingFishing(user, item);
    }

    if (skillsModule.getSkillHandler().isFishFishing(item.getType())) {
      handleFishingFishing(user, item);
    }
  }


  private void handleFarmingFishing(User user, Item item) {
    double exp = skillsModule.getSkillHandler().getFarmingFish().get(item.getType());
    user.getSkills().getFarming().addExp(exp, SkillExpGainCause.FISH);
  }

  private void handleMiningFishing(User user, Item item) {
    double exp = skillsModule.getSkillHandler().getMiningFish().get(item.getType());
    user.getSkills().getMining().addExp(exp, SkillExpGainCause.FISH);
  }

  private void handleCombatFishing(User user, Item item) {
    double exp = skillsModule.getSkillHandler().getCombatFish().get(item.getType());
    user.getSkills().getCombat().addExp(exp, SkillExpGainCause.FISH);
  }

  private void handleWoodcuttingFishing(User user, Item item) {
    double exp = skillsModule.getSkillHandler().getWoodcuttingFish().get(item.getType());
    user.getSkills().getWoodcutting().addExp(exp, SkillExpGainCause.FISH);
  }

  private void handleFishingFishing(User user, Item item) {
    double exp = skillsModule.getSkillHandler().getFishingFish().get(item.getType());
    user.getSkills().getFishing().addExp(exp, SkillExpGainCause.FISH);
  }

}
