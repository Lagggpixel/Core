package me.lagggpixel.core.modules.economy.events;


import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class BalanceTopUpdateEvent extends Event {

  @Getter
  private static final HandlerList handlerList = new HandlerList();

  private final long timeStarted;
  private final long timeEnded;
  private final long timeTaken;

  public BalanceTopUpdateEvent(long timeStarted, long timeEnded) {
    this.timeStarted = timeStarted;
    this.timeEnded = timeEnded;
    this.timeTaken = timeEnded - timeStarted;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
