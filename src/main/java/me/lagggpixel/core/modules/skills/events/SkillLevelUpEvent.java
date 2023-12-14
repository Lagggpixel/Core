package me.lagggpixel.core.modules.skills.events;

import lombok.Getter;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkillLevelUpEvent extends Event {

  @Getter
  private static HandlerList handlerList = new HandlerList();

  @Getter
  private final Player player;
  @Getter
  private final SkillType skillType;
  @Getter
  private final int level;

  /**
   * @param player Player that leveled up
   * @param skillType  The skillType that the player leveled up in
   * @param level  The level that the player is at after leveling
   */
  public SkillLevelUpEvent(Player player, SkillType skillType, int level) {
    this.player = player;
    this.skillType = skillType;
    this.level = level;
  }


  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }

}
