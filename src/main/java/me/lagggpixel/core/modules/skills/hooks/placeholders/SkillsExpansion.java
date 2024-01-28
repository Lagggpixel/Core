/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skills.hooks.placeholders;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.interfaces.ICorePlaceholderExpansion;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class SkillsExpansion extends ICorePlaceholderExpansion {

  @Override
  public @NotNull String getIdentifier() {
    return "coreSkill";
  }

  @Override
  public String onRequest(OfflinePlayer player, @NotNull String params) {
    User user = Main.getUser(player.getUniqueId());
    if (params.equalsIgnoreCase("average")) {
      return String.format("%.1f", user.getSkills().getSkillAverage());
    }

    String strSkillType = params.split("(?=[A-Z])")[0];
    try {
      SkillType skillType = SkillType.valueOf(strSkillType.toUpperCase());
      String[] paramArr = params.split("(?=[A-Z])");
      StringBuilder requestStrBuilder = new StringBuilder();
      String[] requestArr = Arrays.copyOfRange(paramArr, 1, paramArr.length);
      for (String s : requestArr) {
        requestStrBuilder.append(s);
      }
      String request = requestStrBuilder.toString();

      if (request.equalsIgnoreCase("level")) {
        int level = user.getSkills().getSkills().get(skillType).getLevel();
        return String.valueOf(level);
      } else if (request.equalsIgnoreCase("xp")) {
        double xp = user.getSkills().getSkills().get(skillType).getLevelExp();
        return String.format("%.1f", xp);
      } else if (request.equalsIgnoreCase("progress")) {
        double xp = user.getSkills().getSkills().get(skillType).getLevelExp();
        long levelUpExp = SkillHandler.getSkillExpPerLevel().get(user.getSkills().getSkills().get(skillType).getLevel() + 1);
        double percentage = ((float) xp / (float) levelUpExp) * 100;
        return String.format("%.1f", percentage);
      }
    } catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
      return null;
    }

    return null;
  }
}
