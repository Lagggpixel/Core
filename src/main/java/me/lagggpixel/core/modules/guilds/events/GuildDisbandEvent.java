package me.lagggpixel.core.modules.guilds.events;

import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildDisbandEvent extends GuildEvents {
  
  public GuildDisbandEvent(Player player, Guild guild) {
    super(player, guild);
  }
  
  @Override
  public boolean isCancelled() {
    return false;
  }
  
  @Override
  public void setCancelled(boolean b) {
  
  }
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return null;
  }
}
