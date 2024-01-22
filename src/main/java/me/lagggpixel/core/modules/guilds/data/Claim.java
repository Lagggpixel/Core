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
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Setter
@SuppressWarnings("unused")
@Getter
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class Claim {
  private int x1;
  private int x2;
  private int z1;
  private int z2;
  private int value;
  private World world;
  private Location cornerOne;
  private Location cornerTwo;
  private Location cornerThree;
  private Location cornerFour;
  private Guild owner;
  private List<UUID> players;
  private String id;
  private boolean claimExplosions;
  
  public Claim(String id, Guild owner, int x1, int x2, int z1, int z2, World world, int value) {
    
    this.owner = owner;
    
    this.x1 = x1;
    this.x2 = x2;
    this.z1 = z1;
    this.z2 = z2;
    
    this.id = id;
    this.world = world;
    
    this.players = new ArrayList<>();
    
    this.cornerOne = new Location(world, x1, 82.0D, z1);
    this.cornerTwo = new Location(world, x2, 82.0D, z1);
    this.cornerThree = new Location(world, x2, 82.0D, z2);
    this.cornerFour = new Location(world, x1, 82.0D, z2);
    
    this.value = value;
    
  }
  
  
  public boolean overlaps(double x1, double z1, double x2, double z2) {
    double[] dim = new double[2];
    
    int X1 = Math.min(getX1(), getX2());
    int Z1 = Math.min(getZ1(), getZ2());
    int X2 = Math.max(getX1(), getX2());
    int Z2 = Math.max(getZ1(), getZ2());
    
    dim[0] = x1;
    dim[1] = x2;
    
    Arrays.sort(dim);
    
    if (X1 > dim[1] || X2 < dim[0]) {
      return false;
    }
    
    dim[0] = z1;
    dim[1] = z2;
    
    Arrays.sort(dim);
    return !(Z1 > dim[1]) && !(Z2 < dim[0]);
  }
  
  
  public boolean isNearby(@NotNull Location l) {
    
    int CLAIM_BUFFER = 10;
    
    if (!(getWorld() == (new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).getWorld())) {
      return false;
    }
    return isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(0.0D, 0.0D, CLAIM_BUFFER), false) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(CLAIM_BUFFER, 0.0D, 0.0D), false) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(0.0D, 0.0D, -CLAIM_BUFFER), true) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-CLAIM_BUFFER, 0.0D, 0.0D), true) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-CLAIM_BUFFER, 0.0D, CLAIM_BUFFER), false) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(CLAIM_BUFFER, 0.0D, -CLAIM_BUFFER), false) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-CLAIM_BUFFER, 0.0D, -CLAIM_BUFFER), false) || isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(CLAIM_BUFFER, 0.0D, CLAIM_BUFFER), false);
  }
  
  
  public boolean isGlitched() {
    for (Claim claim : GuildModule.getInstance().getClaimManager().getClaims()) {
      if (claim != this) {
        if (claim.isInside(this.cornerOne, false) || claim.isInside(this.cornerTwo, false) || claim.isInside(this.cornerThree, false) || claim.isInside(this.cornerFour, false)) {
          return true;
        }
        if (isInside(claim.getCornerOne(), false) || isInside(claim.getCornerTwo(), false) || isInside(claim.getCornerThree(), false) || isInside(claim.getCornerFour(), false)) {
          return true;
        }
      }
    }
    return false;
  }
  
  
  public boolean isInside(Location loc, boolean player) {
    if (loc.getWorld() == getWorld()) {
      int x1 = Math.min(getX1(), getX2());
      int z1 = Math.min(getZ1(), getZ2());
      int x2 = Math.max(getX1(), getX2());
      int z2 = Math.max(getZ1(), getZ2());
      
      if (player) {
        x2++;
        z2++;
      }
      return (loc.getX() >= x1 && loc.getX() <= x2 && loc.getZ() >= z1 && loc.getZ() <= z2);
    }
    return false;
  }
}



