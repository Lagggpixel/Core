package me.lagggpixel.core.modules.skills.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Skills {
  @SerializedName("PlayerUniqueID")
  @Expose
  @NotNull
  private final UUID playerUuid;
  @SerializedName("Mining")
  @Expose
  private Skill mining;
  @SerializedName("Farming")
  @Expose
  private Skill farming;
  @SerializedName("Combat")
  @Expose
  private Skill combat;
  @SerializedName("Fishing")
  @Expose
  private Skill fishing;
  @SerializedName("Woodcutting")
  @Expose
  private Skill woodcutting;

  private Map<SkillType, Skill> skills;

  public Skills(@NotNull UUID playerUuid) {
    this.playerUuid = playerUuid;
    this.mining = new Skill(this.playerUuid, SkillType.MINING);
    this.mining.initSkillLevel();
    this.farming = new Skill(this.playerUuid, SkillType.FARMING);
    this.farming.initSkillLevel();
    this.combat = new Skill(this.playerUuid, SkillType.COMBAT);
    this.combat.initSkillLevel();
    this.fishing = new Skill(this.playerUuid, SkillType.FISHING);
    this.fishing.initSkillLevel();
    this.woodcutting = new Skill(this.playerUuid, SkillType.WOODCUTTING);
    this.woodcutting.initSkillLevel();
  }

  public Map<SkillType, Skill> getSkills() {
    if (skills == null) {
      skills = Map.of(
          SkillType.MINING, mining,
          SkillType.FARMING, farming,
          SkillType.COMBAT, combat,
          SkillType.FISHING, fishing,
          SkillType.WOODCUTTING, woodcutting);
    }
    return skills;
  }

  public double getSkillAverage() {
    int totalSkillLevel = 0;

    for (Skill skill : getSkills().values()) {
      totalSkillLevel += skill.getLevel();
    }

    int totalSkillCount = getSkills().keySet().size();

    return (float) totalSkillLevel / totalSkillCount;
  }
}
