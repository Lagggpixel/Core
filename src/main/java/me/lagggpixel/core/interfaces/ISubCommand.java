package me.lagggpixel.core.interfaces;

import org.bukkit.command.CommandSender;

public interface ISubCommand {
  void execute(CommandSender commandSender, String[] args);
}