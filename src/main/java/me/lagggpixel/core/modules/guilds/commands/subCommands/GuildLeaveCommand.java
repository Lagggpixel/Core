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
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GuildLeaveCommand implements ISubCommand {

  private final GuildModule guildModule;
  private final GuildHandler guildHandler;

  public GuildLeaveCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
    guildHandler = this.guildModule.getGuildHandler();
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    Guild guild = this.guildHandler.getGuildFromPlayer(sender);

    if (guild == null) {
      sender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    if (guild.isLeader(sender.getUniqueId())) {
      sender.sendMessage(Lang.GUILD_LEAVE_LEADER_CANT_LEAVE.toComponentWithPrefix());
      return;
    }

    if (guild.getOfficers().contains(sender.getUniqueId())) {
      guild.getOfficers().remove(sender.getUniqueId());
    } else {

      guild.getMembers().remove(sender.getUniqueId());
    }
    guild.sendMessage(Lang.GUILD_LEAVE_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName())));
    sender.sendMessage(Lang.GUILD_LEAVE_SUCCESS_NOTIFY.toComponentWithPrefix());
    return;
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
