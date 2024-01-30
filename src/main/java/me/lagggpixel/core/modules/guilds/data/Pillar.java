/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.data;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.guilds.GuildModule;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Setter
@SuppressWarnings({"unused", "deprecation"})
@Getter
public class Pillar {
  private ClaimProfile profile;
  private Material blockType;
  private byte data;
  private ArrayList<Integer> ints;
  private Location location;
  private String ID;
  
  public Pillar(ClaimProfile profile, Material blockType, byte data, Location location, String ID) {
    this.profile = profile;
    this.location = location;
    this.blockType = blockType;
    this.data = data;
    this.ints = new ArrayList<>();
    this.ID = ID;
    GuildModule.getInstance().getPillarManager().getPillars().add(this);
  }

  public Pillar sendPillar() {
    int x = this.location.getBlockX();
    int z = this.location.getBlockZ();
    for (int i = 0; i <= getLocation().getWorld().getMaxHeight(); i++) {
      Location location = new Location(getLocation().getWorld(), x, i, z);
      if (location.getBlock().getType() == Material.AIR &&
          this.profile.getPlayer() != null) {
        if (this.ints.contains(location.getBlockY())) {
          this.profile.getPlayer().sendBlockChange(location, this.blockType, this.data);
          this.profile.getPlayer().sendBlockChange(location.add(0.0D, 2.0D, 0.0D), Material.GLASS, (byte) 0);
        } else {
          this.profile.getPlayer().sendBlockChange(location, Material.GLASS, (byte) 0);
          this.ints.add(location.getBlockY() + 2);
        }
      }
    }
    
    return this;
  }
  
  
  public void removePillar() {
    int x = this.location.getBlockX();
    int z = this.location.getBlockZ();
    for (int i = 0; i <= getLocation().getWorld().getMaxHeight(); i++) {
      Location location = new Location(getLocation().getWorld(), x, i, z);
      if (location.getBlock().getType() == Material.AIR) {
        if (profile.getPlayer() != null) {
          this.profile.getPlayer().sendBlockChange(location, Material.AIR, (byte) 0);
        }
      }
    }
  }
  
  public void delete() {
    int x = this.location.getBlockX();
    int z = this.location.getBlockZ();
    for (int i = 0; i <= getLocation().getWorld().getMaxHeight(); i++) {
      Location location = new Location(getLocation().getWorld(), x, i, z);
      if (location.getBlock().getType() == Material.AIR)
        location.getBlock().getState().update();
    }
  }
}


