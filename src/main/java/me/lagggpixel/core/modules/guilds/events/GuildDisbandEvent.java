package me.lagggpixel.core.modules.guilds.events;

import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;

public class GuildDisbandEvent extends GuildEvents {
  public GuildDisbandEvent(Player player, Guild guild) {
    super(player, guild);
  }
}
