package me.lagggpixel.core.modules.skills.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.skills.enums.SkillType;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Skill {

  @SerializedName("SkillType")
  @Expose
  private final SkillType skillType;
  @SerializedName("SkillLevel")
  @Expose
  private final int level;
  @SerializedName("SkillTotalExperience")
  @Expose
  private final int totalExp;
  @SerializedName("SkillCurrentLevelExperience")
  @Expose
  private final int levelExp;

  public Skill(SkillType skillType) {
    this.skillType = skillType;
    this.level = 0;
    this.totalExp = 0;
    this.levelExp = 0;
  }
}
