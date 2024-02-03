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
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class GuildCreateCommand implements ISubCommand {
  private final GuildModule guildModule;
  private final GuildHandler guildHandler;
  
  public GuildCreateCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
    guildHandler = this.guildModule.getGuildHandler();
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    User user = Main.getUser(player);
    UUID playerUniqueId = player.getUniqueId();

    if (args.length != 2) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    if (guildHandler.getGuildFromPlayerUUID(playerUniqueId) != null) {
      commandSender.sendMessage(Lang.GUILD_ALREADY_IN_GUILD.toComponentWithPrefix());
      return;
    }
    
    String guildName = args[1];
    
    if (guildName.length() < 3 || guildName.length() > 16 || !Character.isLetter(guildName.charAt(0))) {
      commandSender.sendMessage(Lang.GUILD_NAME_INVALID.toComponentWithPrefix());
      return;
    }
    
    for (Guild guild : guildHandler.getGuilds()) {
      if (guild.getName().equalsIgnoreCase(guildName)) {
        user.sendMessage(Lang.GUILD_NAM_EXISTS.toComponentWithPrefix());
        return;
      }
    }
    
    Guild newGuild = guildHandler.createGuild(guildName, player);
    if (newGuild == null) {
      commandSender.sendMessage(Lang.GUILD_FAILED_TO_CREATE.toComponentWithPrefix());
      return;
    }
    guildHandler.getGuilds().add(newGuild);
    commandSender.sendMessage(Lang.GUILD_CREATED.toComponentWithPrefix(Map.of("%guild%", newGuild.getName())));
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
