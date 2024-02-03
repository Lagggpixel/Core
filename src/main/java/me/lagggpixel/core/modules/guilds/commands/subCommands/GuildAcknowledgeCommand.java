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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Lagggpixel
 * @since February 03, 2024
 */
public class GuildAcknowledgeCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildAcknowledgeCommand(GuildModule guildModule) {
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

    User user = Main.getUser(sender);

    switch (args[1]) {
      case "piston":
        sender.sendMessage(Lang.GUILD_ACKNOWLEDGE_PISTON.toComponentWithPrefix());
        user.getUserPreference().setAcknowledgedPistonRules(true);
        break;
      case "tnt":
        sender.sendMessage(Lang.GUILD_ACKNOWLEDGE_TNT.toComponentWithPrefix());
        user.getUserPreference().setAcknowledgedTntRules(true);
        break;
      default:
        sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
        break;
    }
  }

  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    if (args.length == 2) {
      return List.of("piston", "tnt");
    }
    return null;
  }
}
