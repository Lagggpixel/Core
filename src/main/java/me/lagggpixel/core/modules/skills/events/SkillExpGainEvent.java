package me.lagggpixel.core.modules.skills.events;

import lombok.Getter;
import me.lagggpixel.core.modules.skills.enums.SkillExpGainCause;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SkillExpGainEvent extends Event {

  @Getter
  private static HandlerList handlerList = new HandlerList();

  @Getter
  private final UUID uuid;
  @Getter
  private final SkillType skillType;
  @Getter
  private final double exp_gained;
  @Getter
  private final SkillExpGainCause cause;

  /**
   * @param uuid       Player that leveled up
   * @param skillType  The skillType that the player leveled up in
   * @param exp_gained The amount of exp that the player gained
   * @param cause      The cause of the exp gain
   */
  public SkillExpGainEvent(UUID uuid, SkillType skillType, double exp_gained, SkillExpGainCause cause) {
    this.uuid = uuid;
    this.skillType = skillType;
    this.exp_gained = exp_gained;
    this.cause = cause;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
