/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.economy.events;


import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
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
