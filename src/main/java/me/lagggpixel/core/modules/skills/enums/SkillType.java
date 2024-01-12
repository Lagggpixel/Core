package me.lagggpixel.core.modules.skills.enums;

import org.apache.commons.lang3.StringUtils;

public enum SkillType {

  FARMING,
  MINING,
  COMBAT,
  FISHING,
  WOODCUTTING;

  public String getName() {
    return StringUtils.capitalize(name().toLowerCase());
  }

}
