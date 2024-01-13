package me.lagggpixel.core.interfaces;

import org.bukkit.command.TabExecutor;

import java.util.List;

public interface ICommandClass extends TabExecutor {
  
  String getCommandName();

  String getCommandDescription();

  List<String> getCommandAliases();

  String getCommandPermission();

  String getUsage();
}
