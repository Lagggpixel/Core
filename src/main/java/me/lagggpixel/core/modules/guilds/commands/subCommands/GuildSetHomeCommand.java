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
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildSetHomeCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildSetHomeCommand(GuildModule guildModule) {
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

    if (guild == null) {
      commandSender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    if (guild.isLeader(sender.getUniqueId()) || guild.getOfficers().contains(sender.getUniqueId())) {
      Location location = sender.getLocation();
      for (Claim claim : guild.getClaims()) {
        if (claim.isInside(location, true)) {
          guild.setHome(location);
          senderUser.sendMessage(Lang.GUILD_SETHOME_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%x%", location.getBlockX() + "", "%y%", location.getBlockY() + "", "%z%", location.getBlockZ() + "")));
          guild.sendMessage(Lang.GUILD_SETHOME_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%x%", location.getBlockX() + "", "%y%", location.getBlockY() + "", "%z%", location.getBlockZ() + "")));
          return;
        }
      }
      senderUser.sendMessage(Lang.GUILD_SETHOME_NOT_IN_CLAIM.toComponentWithPrefix());
    } else {
      senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
    }

  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
