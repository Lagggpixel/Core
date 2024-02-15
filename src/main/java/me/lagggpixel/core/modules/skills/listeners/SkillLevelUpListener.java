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
import me.lagggpixel.core.modules.skills.events.SkillLevelUpEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkillLevelUpListener implements Listener {

  private final SkillsModule module;

  public SkillLevelUpListener(SkillsModule module) {
    Main.getPluginManager().registerEvents(this, Main.getInstance());
    this.module = module;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void SkillLevelUpEvent(SkillLevelUpEvent event) {
    Component component = Lang.SKILL_LEVEL_UP.toComponent(Map.of("%skill%", event.getSkillType().getName(),
        "%level_before%", String.valueOf(event.getLevel() - 1),
        "%level_after%", String.valueOf(event.getLevel()),
        "%coins%", String.valueOf(event.getReward())));

    Main.getUser(event.getUuid()).sendMessage(component);
  }
}
