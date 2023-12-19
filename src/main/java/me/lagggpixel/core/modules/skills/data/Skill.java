package me.lagggpixel.core.modules.skills.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.skills.enums.SkillExpGainCause;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import me.lagggpixel.core.modules.skills.events.SkillExpGainEvent;
import me.lagggpixel.core.modules.skills.events.SkillLevelUpEvent;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Skill {
  @SerializedName("PlayerUniqueID")
  @Expose
  @NotNull
  private final UUID playerUuid;
  @SerializedName("SkillType")
  @Expose
  private final SkillType skillType;
  @SerializedName("SkillLevel")
  @Expose
  private int level;
  @SerializedName("SkillTotalExperience")
  @Expose
  private double totalExp;
  @SerializedName("SkillCurrentLevelExperience")
  @Expose
  private double levelExp;
  
  public Skill(@NotNull UUID playerUuid, SkillType skillType) {
    this.playerUuid = playerUuid;
    this.skillType = skillType;
    this.level = 0;
    this.totalExp = 0;
    this.levelExp = 0;
  }
  
  public void addExp(double expGained, SkillExpGainCause cause) {
    this.totalExp += totalExp + expGained;
    SkillExpGainEvent skillExpGainEvent = new SkillExpGainEvent(playerUuid, this.skillType, expGained, cause);
    Bukkit.getServer().getPluginManager().callEvent(skillExpGainEvent);
    updateSkillLevel();
  }
  
  public void updateSkillLevel() {
    boolean hasUpdated = true;
    int money = 0;
    while (hasUpdated) {
      long levelUpExp = SkillHandler.getSkillExpPerLevel().get(this.level + 1);
      if (this.levelExp >= levelUpExp) {
        this.level++;
        this.levelExp -= levelUpExp;
        money += SkillHandler.getMoneyPerLevel().get(this.level);
        SkillLevelUpEvent skillLevelUpEvent = new SkillLevelUpEvent(playerUuid, this.skillType, this.level, money);
        Bukkit.getServer().getPluginManager().callEvent(skillLevelUpEvent);
      } else {
        hasUpdated = false;
      }
    }
    EconomyManager.getInstance().deposit(playerUuid, money);
  }
  
  public void initSkillLevel() {
    int originalSkillLevel = this.level;
    this.level = 0;
    this.levelExp = this.totalExp;
    boolean hasUpdated = true;
    while (hasUpdated) {
      long levelUpExp = SkillHandler.getSkillExpPerLevel().get(this.level + 1);
      if (this.levelExp >= levelUpExp) {
        this.level++;
        this.levelExp -= levelUpExp;
      } else {
        hasUpdated = false;
      }
    }
    int newSkillLevel = this.level;
    int money = 0;
    if (originalSkillLevel >= newSkillLevel) {
      return;
    }
    for (int i = originalSkillLevel+1; i <= newSkillLevel; i++) {
      money += SkillHandler.getMoneyPerLevel().get(i);
      SkillLevelUpEvent skillLevelUpEvent = new SkillLevelUpEvent(playerUuid, this.skillType, i, money);
      Bukkit.getServer().getPluginManager().callEvent(skillLevelUpEvent);
    }
    EconomyManager.getInstance().deposit(playerUuid, money);
  }
}
