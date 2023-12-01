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


