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
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.DiscordModule;
import org.javacord.api.entity.channel.ChannelCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Level;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
@Getter
public enum TicketType {

  MINECRAFT_SUPPORT("mc", 1151605379118145536L),
  BUG_REPORT("bug", 1200944822198997125L),
  DISCORD_SUPPORT("dc", 1151605380091232317L),
  APPLICATION("app", 1200945033973616650L),
  APPEAL("appeal", 1153133993731043359L);

  private final String id;
  private final long catagoryId;

  @NotNull
  public ChannelCategory getCatagory() {
    Optional<ChannelCategory> catagory = DiscordModule.discordHandler.server.getChannelCategoryById(getCatagoryId());
    if (catagory.isEmpty()) {
      Main.log(Level.SEVERE, "Ticket catagory " + getCatagoryId() + " does not exist for ticket type " + name().toLowerCase() + "!");
      Main.getInstance().onDisable();
    }
    return catagory.get();
  }

  TicketType(String id, long catagoryId) {
    this.id = id;
    this.catagoryId = catagoryId;
  }
}
