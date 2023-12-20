package me.lagggpixel.core.modules.guilds.hooks.placeholders;

import me.lagggpixel.core.interfaces.ICorePlaceholderExpansion;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.ClaimManager;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GuildExpansion extends ICorePlaceholderExpansion {

  private final GuildModule guildModule = GuildModule.getInstance();
  private final GuildHandler guildHandler = guildModule.getGuildHandler();
  private final ClaimManager claimManager = guildModule.getClaimManager();

  @Override
  public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
    UUID uuid = offlinePlayer.getUniqueId();

    switch (params) {
      case "guild_current_location" -> {
        if (!offlinePlayer.isOnline()) {
          return Lang.GUILD_PLACEHOLDER_SYSTEM.getDef();
        }
        Player player = (Player) offlinePlayer;
        Claim claim = claimManager.getClaimAt(player.getLocation());
        if (claim == null) {
          return Lang.GUILD_PLACEHOLDER_SYSTEM.getDef();
        }

        Guild guild = null;
        for (Guild guilds : guildHandler.getGuilds()) {
          if (guilds.getClaims().contains(claim)) {
            guild = guilds;
            break;
          }
        }

        if (guild == null) {
          return Lang.GUILD_PLACEHOLDER_SYSTEM.getDef();
        }

        Guild playerGuild = guildHandler.getGuildFromPlayer(player);
        if (playerGuild == null) {
          return Lang.GUILD_PLACEHOLDER_SYSTEM.getDef();
        }
        if (playerGuild == guild) {
          return Lang.GUILD_PLACEHOLDER_COLOR_FRIENDLY.getDef() + guild.getName();
        }
        if (playerGuild.getAllies().contains(guild) || guild.getAllies().contains(playerGuild)) {
          return Lang.GUILD_PLACEHOLDER_COLOR_ALLY.getDef() + guild.getName();
        }
        return Lang.GUILD_PLACEHOLDER_COLOR_ENEMY.getDef() + guild.getName();
      }

      case "guildName" -> {
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
