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
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.interfaces.ISubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class GuildDisbandCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildDisbandCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    GuildHandler guildHandler = guildModule.getGuildHandler();
    UUID playerUniqueId = sender.getUniqueId();
    Guild guild = guildHandler.getGuildFromPlayerUUID(playerUniqueId);
    if (guild == null) {
      commandSender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }
    String guildName = guildHandler.getGuildFromPlayerUUID(playerUniqueId).getName();
    if (!guild.getLeader().equals(playerUniqueId)) {
      commandSender.sendMessage(Lang.GUILD_NOT_LEADER_DISBAND.toComponentWithPrefix());
      return;
    }
    
    guildHandler.disbandGuild(sender, guild);
    
    commandSender.sendMessage(Lang.GUILD_DISBANDED_LEADER.toComponentWithPrefix(Map.of("%guild%", guildName)));
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
