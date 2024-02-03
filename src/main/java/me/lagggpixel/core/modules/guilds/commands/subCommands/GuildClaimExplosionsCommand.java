/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since February 03, 2024
 */
public class GuildClaimExplosionsCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildClaimExplosionsCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    if (args.length != 2) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    Guild guild = guildModule.getGuildHandler().getGuildFromPlayerUUID(sender.getUniqueId());
    if (guild == null) {
      sender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    boolean toggleStatus;

    switch (args[1]) {
      case "true":
        toggleStatus = true;
        break;
      case "false":
        toggleStatus = false;
        break;
      default:
        sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        return;
    }


    guild.getClaims().forEach(claim -> claim.setClaimExplosions(toggleStatus));
    sender.sendMessage(Lang.GUILD_CLAIM_EXPLOSIONS_CHANGED.toComponentWithPrefix(Map.of(
            "%status%", toggleStatus ? "&aenabled" : "&cdisabled",
            "%player%", sender.getName()
        )
    ));
  }

  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    if (args.length == 2) {
      return List.of("true", "false");
    }
    return null;
  }
}
