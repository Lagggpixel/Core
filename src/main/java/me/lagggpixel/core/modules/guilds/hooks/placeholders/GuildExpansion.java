/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.hooks.placeholders;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICorePlaceholderExpansion;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildExpansion extends ICorePlaceholderExpansion {

  private final GuildModule guildModule = GuildModule.getInstance();
  private final GuildHandler guildHandler = guildModule.getGuildHandler();

  @Override
  public @NotNull String getIdentifier() {
    return "coreGuild";
  }

  @Override
  public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
    UUID uuid = offlinePlayer.getUniqueId();

    switch (params) {

      case "name" -> {
        Guild playerGuild = guildHandler.getGuildFromPlayerUUID(uuid);
        if (playerGuild == null) {
          return Lang.GUILD_PLACEHOLDER_NO_GUILD.getDef();
        }
        return playerGuild.getName();
      }

      default -> {
        return null;
      }
    }
  }
}
