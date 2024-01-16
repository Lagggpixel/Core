package me.lagggpixel.core.data;

import lombok.Data;
import lombok.Getter;
import me.lagggpixel.core.utils.HologramUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;


@Data
@Getter
public class Hologram {
  
  private ArmorStand armorStand;
  
  private final String id;
  private Component name;
  private Location location;
  
  public Hologram(String id, Component name, Location location) {
    this.id = id;
    this.name = name;
    this.location = location;
    
    spawnHologram();
  }
  
  private void spawnHologram() {
    this.armorStand = location.getWorld().spawn(location, ArmorStand.class);
    this.armorStand.setVisible(false);
    this.armorStand.setGravity(false);
    this.armorStand.setMarker(true);
    this.armorStand.setCustomNameVisible(true);
    this.armorStand.customName(name);
    HologramUtils.holograms.put(id, this);
  }
  
  public void destroy() {
    HologramUtils.holograms.remove(id);
    if (this.armorStand != null) {
      this.armorStand.remove();
      this.armorStand = null;
    }
  }
  
  public void setName(Component name) {
    this.name = name;
    this.armorStand.customName(name);
    this.armorStand.setCustomNameVisible(true);
  }
  
  public void setLocation(Location location) {
    this.location = location;
    this.armorStand.teleport(location);
  }
}
