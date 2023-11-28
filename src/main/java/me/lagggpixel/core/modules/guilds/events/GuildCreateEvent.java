package me.lagggpixel.core.modules.guilds.events;

import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildCreateEvent extends GuildEvents {
  private boolean cancelled;
  
  public GuildCreateEvent(Player player, Guild guild) {
    super(player, guild);
  }
  
  @Override
  public boolean isCancelled() {
    return cancelled;
  }
  
  @Override
  public void setCancelled(boolean b) {
    cancelled = b;
  }
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
