package me.lagggpixel.core.modules.skipnight.objects;

import lombok.Getter;

import java.util.UUID;

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
