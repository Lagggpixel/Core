/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
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
