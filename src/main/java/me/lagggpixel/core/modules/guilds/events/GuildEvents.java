package me.lagggpixel.core.modules.guilds.events;

import lombok.Getter;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildEvents extends Event implements Cancellable {
  @Getter
  protected final Player player;
  @Getter
  protected final Guild guild;
  
  protected HandlerList handlerList;
  protected boolean cancelled;
  
  public GuildEvents(Player player, Guild guild) {
    this.player = player;
    this.guild = guild;
    handlerList = new HandlerList();
    cancelled = false;
  }
  
  @Override
  public boolean isCancelled() {
    return cancelled;
  }
  
  @Override
  public void setCancelled(boolean b) {
    this.cancelled = b;
  }
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
