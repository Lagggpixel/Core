package me.lagggpixel.core.modules.skills.hooks.placeholders;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.interfaces.ICorePlaceholderExpansion;
import me.lagggpixel.core.modules.skills.enums.SkillType;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SkillsExpansion extends ICorePlaceholderExpansion {
  @Override
  public String onRequest(OfflinePlayer player, @NotNull String params) {

    if (params.equalsIgnoreCase("skillAverage")) {
      // todo: return skill average
    }

    String strSkillType = params.split("(?=[A-Z])")[1];
    try {
      SkillType skillType = SkillType.valueOf(strSkillType.toUpperCase());
      String[] paramArr = params.split("(?=[A-Z])");
      StringBuilder requestStrBuilder = new StringBuilder();
      String[] requestArr = Arrays.copyOfRange(paramArr, 2, paramArr.length);
      for (String s : requestArr) {
        requestStrBuilder.append(s);
      }
      String request = requestStrBuilder.toString();

      User user = Main.getUser(player.getUniqueId());

      if (request.equalsIgnoreCase("level")) {
        int level = user.getSkills().getSkills().get(skillType).getLevel();
        return String.valueOf(level);
      }
      else if (request.equalsIgnoreCase("xp")) {
        double xp = user.getSkills().getSkills().get(skillType).getLevelExp();
        return String.format("%.1f", xp);
      }
      else if (request.equalsIgnoreCase("progress")) {
        double xp = user.getSkills().getSkills().get(skillType).getLevelExp();
        long levelUpExp = SkillHandler.getSkillExpPerLevel().get(user.getSkills().getSkills().get(skillType).getLevel() + 1);
        double percentage = (float) ((float) xp / (float) levelUpExp) * 100;
        return String.format("%.1f", percentage);
      }
    }
    catch (NullPointerException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
      return null;
    }

    return null;
  }
}
