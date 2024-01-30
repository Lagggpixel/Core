/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skills.events;

import lombok.Getter;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkillLevelUpEvent extends Event {

  @Getter
  private static HandlerList handlerList = new HandlerList();

  @Getter
  private final UUID uuid;
  @Getter
  private final SkillType skillType;
  @Getter
  private final int level;
  @Getter
  private final int reward;

  /**
   * @param uuid      Player that leveled up
   * @param skillType The skillType that the player leveled up in
   * @param level     The level that the player is at after leveling
   */
  public SkillLevelUpEvent(UUID uuid, SkillType skillType, int level, int reward) {
    this.uuid = uuid;
    this.skillType = skillType;
    this.level = level;
    this.reward = reward;
  }


  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }

}
