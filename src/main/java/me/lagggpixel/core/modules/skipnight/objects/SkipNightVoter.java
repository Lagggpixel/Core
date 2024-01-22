/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skipnight.objects;

import lombok.Getter;

import java.util.UUID;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class SkipNightVoter {
  
  private final UUID uuid;
  @Getter
  private int vote;
  
  public SkipNightVoter(UUID uuid) {
    this.uuid = uuid;
    vote = 0;
  }
  
  public void voteYes() {
    vote = 1;
  }
  
  public void voteNo() {
    vote = -1;
  }
  
  public int resetVote() {
    int vote = this.vote;
    this.vote = 0;
    return vote;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    SkipNightVoter skipNightVoter = (SkipNightVoter) o;
    
    return uuid.equals(skipNightVoter.uuid);
  }
  
  @Override
  public int hashCode() {
    return uuid.hashCode();
  }
}
