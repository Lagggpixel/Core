package me.lagggpixel.core.modules.guilds.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
  void execute(CommandSender sender, String[] args);
}