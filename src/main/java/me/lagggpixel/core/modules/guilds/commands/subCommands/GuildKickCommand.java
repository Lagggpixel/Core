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

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class GuildKickCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildKickCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    User senderUser = Main.getUser(sender.getUniqueId());
    Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);
    
    
    if (args.length >= 1) {
      
      if (guild == null) {
        senderUser.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
        return;
      }
      
      if (!guild.isLeader(sender.getUniqueId()) && !guild.getOfficers().contains(sender.getUniqueId())) {
        senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
        return;
      }
      StringBuilder sb = new StringBuilder();
      for (String arg : args) {
        sb.append(arg).append(" ");
      }
      String name = sb.toString().trim().replace(" ", "");
      
      if (guild.getPlayer(name) == null) {
        senderUser.sendMessage(Lang.GUILD_KICK_NOT_PART_OF_GUILD.toComponentWithPrefix(Map.of("%player%", name)));
        return;
      }
      UUID uuid = guild.getPlayer(name).getUniqueId();
      
      if (guild.getOfficers().contains(uuid) && guild.getOfficers().contains(sender.getUniqueId())) {
        senderUser.sendMessage(Lang.GUILD_KICK_CANNOT_KICK_OFFICER.toComponentWithPrefix());
        return;
      }
      if (guild.isLeader(uuid)) {
        senderUser.sendMessage(Lang.GUILD_KICK_CANNOT_KICK_LEADER.toComponentWithPrefix());
        return;
      }
      
      User playerUser = Main.getUser(uuid);
      playerUser.sendMessage(Lang.GUILD_KICK_SUCCESS_NOTIFY.toComponentWithPrefix());
      
      senderUser.sendMessage(Lang.GUILD_KICK_SUCCESS_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%player%", name)));
      guild.sendMessage(Lang.GUILD_KICK_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", name)));
      
      if (guild.getOfficers().contains(uuid)) {
        guild.getOfficers().remove(uuid);
      } else {
        guild.getMembers().remove(uuid);
      }
      return;
    }
    senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    
    if (!(commandSender instanceof Player sender)) {
      return List.of(" ");
    }
    
    Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);
    
    if (guild == null) {
      return List.of(" ");
    }
    
    if (guild.isLeader(sender.getUniqueId()) || guild.getOfficers().contains(sender.getUniqueId())) {
      return List.of(" ");
    }
    
    if (guild.isLeader(sender.getUniqueId())) {
      return guild.getMembers()
          .stream().filter(member -> !guild.getLeader().equals(member))
          .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
          .toList();
    }
    else {
      return guild.getMembers()
          .stream().filter(member -> !guild.getOfficers().contains(member) && !guild.getLeader().equals(member))
          .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
          .toList();
    }
  }
}
