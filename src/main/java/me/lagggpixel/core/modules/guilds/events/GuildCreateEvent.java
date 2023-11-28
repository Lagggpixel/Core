package me.lagggpixel.core.modules.guilds.events;

import lombok.Getter;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuildCreateEvent extends Event implements Cancellable {
  
  @Getter
  private final Guild guild;
  @Getter
  private final Player player;
  
  private final HandlerList handlerList;
  private boolean cancelled;
  
  public GuildCreateEvent(Guild guild, Player player) {
    this.guild = guild;
    this.player = player;
    handlerList = new HandlerList();
    cancelled = false;
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
