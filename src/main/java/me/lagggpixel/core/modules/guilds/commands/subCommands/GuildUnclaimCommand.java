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
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class GuildUnclaimCommand implements ISubCommand {
  
  private final GuildModule guildModule;
  
  public GuildUnclaimCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    User senderUser = Main.getUser(sender.getUniqueId());
    
    for (Claim claim : guildModule.getClaimManager().getClaims()) {
      if (claim.isInside(sender.getLocation(), true)) {
        Guild guild = claim.getOwner();
        if (guild != guildModule.getGuildHandler().getGuildFromPlayer(sender)) {
          senderUser.sendMessage(Lang.GUILD_UNCLAIM_NOT_IN_GUILD_CLAIM.toComponentWithPrefix());
          return;
        }
        if (!guild.getOfficers().contains(sender.getUniqueId()) && !guild.isLeader(sender.getUniqueId())) {
          senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
          return;
        }
        
        if (guild.getHome() != null && claim.isInside(guild.getHome(), false)) {
          guild.setHome(null);
        }
        
        guild.getClaims().remove(claim);
        guildModule.getClaimManager().getClaims().remove(claim);
        senderUser.sendMessage(Lang.GUILD_UNCLAIM_SUCCESS_NOTIFY.toComponentWithPrefix());
        guild.sendMessage(Lang.GUILD_UNCLAIM_SUCCESS_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName())));
        guild.setBalance(guild.getBalance() + claim.getValue());
        return;
      }
    }
    senderUser.sendMessage(Lang.GUILD_UNCLAIM_NOT_IN_GUILD_CLAIM.toComponentWithPrefix());
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
