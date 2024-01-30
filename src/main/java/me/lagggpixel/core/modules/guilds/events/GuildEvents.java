/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.events;

import lombok.Getter;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildEvents extends Event implements Cancellable {
  @Getter
  protected final Player player;
  @Getter
  protected final Guild guild;
  
  @Getter
  protected static HandlerList handlerList = new HandlerList();
  protected boolean cancelled;
  
  public GuildEvents(Player player, Guild guild) {
    this.player = player;
    this.guild = guild;
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
