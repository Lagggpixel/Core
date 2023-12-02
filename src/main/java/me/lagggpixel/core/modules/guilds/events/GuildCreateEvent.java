package me.lagggpixel.core.modules.guilds.events;

import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;

public class GuildCreateEvent extends GuildEvents {
  public GuildCreateEvent(Player player, Guild guild) {
    super(player, guild);
  }
}
