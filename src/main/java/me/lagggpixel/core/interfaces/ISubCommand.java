package me.lagggpixel.core.interfaces;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ISubCommand {
  void execute(CommandSender commandSender, String[] args);
  
  List<String> tabComplete(CommandSender commandSender, String[] args);
}