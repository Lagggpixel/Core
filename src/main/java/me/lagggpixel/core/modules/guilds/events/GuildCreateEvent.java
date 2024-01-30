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

import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.entity.Player;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildCreateEvent extends GuildEvents {
  public GuildCreateEvent(Player player, Guild guild) {
    super(player, guild);
  }
}
