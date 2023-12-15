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
public class Skills {

  @SerializedName("Mining")
  @Expose
  private Skill mining;
  @SerializedName("Farming")
  @Expose
  private Skill farming;
  @SerializedName("Combat")
  @Expose
  private Skill combat;

  public Skills() {
    this.mining = new Skill(SkillType.MINING);
    this.farming = new Skill(SkillType.FARMING);
    this.combat = new Skill(SkillType.FARMING);
  }
}
