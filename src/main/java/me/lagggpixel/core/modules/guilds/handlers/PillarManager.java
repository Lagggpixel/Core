/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.handlers;

import lombok.Getter;
import me.lagggpixel.core.modules.guilds.data.ClaimProfile;
import me.lagggpixel.core.modules.guilds.data.Pillar;

import java.util.HashSet;
import java.util.Iterator;


@Getter
public class PillarManager {
  private final HashSet<Pillar> pillars = new HashSet<>();
  
  public Pillar getPillar(ClaimProfile profile, String ID) {
    for (Pillar pillar : this.pillars) {
      if (pillar.getProfile().getUuid() == profile.getUuid() &&
          pillar.getID().equalsIgnoreCase(ID)) {
        return pillar;
      }
    }
    return null;
  }
  
  public void removeAll() {
    for (Iterator<Pillar> pillars = getPillars().iterator(); pillars.hasNext(); ) {
      pillars.next().removePillar();
      pillars.remove();
    }
  }
  
  
}


