/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

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
