/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.tickets;

import lombok.Getter;
import org.javacord.api.entity.user.User;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Lagggpixel
 * @since February 10, 2024
 */
public class TicketAction {

  @Getter
  private final User creator;
  @Getter
  private final Ticket ticket;
  @Getter
  private final String reason;
  @Nullable
  private User resolver;
  @Getter
  private final Action action;

  public TicketAction(User creator, Ticket ticket, Action action) {
    this.creator = creator;
    this.ticket = ticket;
    this.reason = "Deletion requested by creator";
    this.action = action;
  }

  public TicketAction(User creator, Ticket ticket, @Nullable User resolver, String reason, Action action) {
    this.creator = creator;
    this.ticket = ticket;
    this.resolver = resolver;
    this.reason = reason;
    this.action = action;
  }

  public TicketAction(Ticket ticket, @Nullable User resolver, String reason, Action action) {
    this.creator = null;
    this.ticket = ticket;
    this.resolver = resolver;
    this.reason = reason;
    this.action = action;
  }

  @Nullable
  public User getResolver() {
    return resolver;
  }

  @Getter
  public enum Action {
    CREATE("Ticket Creation Event", Color.GREEN),
    RESOLVE("Ticket Resolve Event", Color.YELLOW),
    CLOSE("Ticket Deletion Event", Color.RED);

    private final String title;
    private final Color color;

    Action(String title, Color color) {
      this.title = title;
      this.color = color;
    }

  }

}