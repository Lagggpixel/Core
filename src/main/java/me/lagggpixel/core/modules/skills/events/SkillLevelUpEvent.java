package me.lagggpixel.core.modules.skills.events;

import lombok.Getter;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
