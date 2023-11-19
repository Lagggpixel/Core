package me.lagggpixel.core.modules.guilds.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
public class Guild {



  private String name;

  private UUID leader;
  private final List<UUID> members;

  public Guild(String name, UUID leader) {
    this.name = name;
    this.leader = leader;

    this.members = List.of(leader);
  }

}
