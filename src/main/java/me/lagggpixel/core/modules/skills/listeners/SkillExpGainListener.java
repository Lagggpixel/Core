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
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skills.events.SkillExpGainEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkillExpGainListener implements Listener {

  private final SkillsModule module;

  public SkillExpGainListener(SkillsModule module) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.module = module;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void SkillExpGainEvent(SkillExpGainEvent event) {
    Player player = Bukkit.getPlayer(event.getUuid());

    if (player == null) {
      return;
    }

    int level = Main.getUser(player).getSkills().getSkill(event.getSkillType()).getLevel();

    Component component = Lang.SKILL_EXP_GAIN.toComponent(Map.of(
        "%skill%", event.getSkillType().getName(),
        "%exp%", String.valueOf(event.getExp_gained())));

    player.sendActionBar(component);
  }
}
