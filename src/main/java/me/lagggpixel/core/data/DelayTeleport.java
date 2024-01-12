package me.lagggpixel.core.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class DelayTeleport {
  
  private final Player player;
  private final Location initialPlayerLocation;
  private final Location location;
  private final int defaultDelay = 5;
  private int currentDelay;
  private final String placeName;
  
  public DelayTeleport(Player player, Location location, int currentDelay, String placeName) {
    this.player = player;
    this.initialPlayerLocation = player.getLocation();
    this.location = location;
    this.currentDelay = currentDelay;
    this.placeName = placeName;
  }
  
  public DelayTeleport(Player player, Location location, String placeName) {
    this.player = player;
    this.initialPlayerLocation = player.getLocation();
    this.location = location;
    this.currentDelay = defaultDelay;
    this.placeName = placeName;
  }
  
  public void minus_delay() {
    this.currentDelay--;
  }
  
}
