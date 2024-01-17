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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
@SuppressWarnings("unused")
@Getter
public class ClaimProfile {
  private UUID uuid;
  private Claim lastInside;
  private Claim inside;
  private int x1;
  private int x2;
  private int z1;
  private int z2;

  public ClaimProfile(UUID uuid) {
    this.uuid = uuid;
  }
  
  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }
}

