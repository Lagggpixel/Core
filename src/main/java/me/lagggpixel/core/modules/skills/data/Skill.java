/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

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

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
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
  private int level = 0;
  @SerializedName("SkillTotalExperience")
  @Expose
  private double totalExp = 0;
  @SerializedName("SkillCurrentLevelExperience")
  @Expose
  private double levelExp = 0;

  public Skill(@NotNull UUID playerUuid, SkillType skillType) {
    this.playerUuid = playerUuid;
    this.skillType = skillType;
  }

  public void addExp(double expGained, SkillExpGainCause cause) {
    this.totalExp = totalExp + expGained;
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
      if (SkillHandler.getSkillExpPerLevel().containsKey(this.level + 1)) {
        long levelUpExp = SkillHandler.getSkillExpPerLevel().get(this.level + 1);
        if (this.levelExp >= levelUpExp) {
          this.level++;
          this.levelExp -= levelUpExp;
        } else {
          hasUpdated = false;
        }
      } else {
        hasUpdated = false;
      }
    }
    int newSkillLevel = this.level;
    int money = 0;
    if (originalSkillLevel >= newSkillLevel) {
      return;
    }
    for (int i = originalSkillLevel + 1; i <= newSkillLevel; i++) {
      money += SkillHandler.getMoneyPerLevel().get(i);
      SkillLevelUpEvent skillLevelUpEvent = new SkillLevelUpEvent(playerUuid, this.skillType, i, money);
      Bukkit.getServer().getPluginManager().callEvent(skillLevelUpEvent);
    }
    EconomyManager.getInstance().deposit(playerUuid, money);
  }
}
