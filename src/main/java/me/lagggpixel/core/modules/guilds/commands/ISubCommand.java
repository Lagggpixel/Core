package me.lagggpixel.core.modules.guilds.commands;

import org.bukkit.command.CommandSender;

public interface ISubCommand {
  void execute(CommandSender commandSender, String[] args);
}