package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.IModule;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class CommandUtils {

  public static void registerCommand(ICommandClass ICommandClass) {
    PluginCommand command = getCommand(ICommandClass.getCommandName());

    command.setAliases(ICommandClass.getCommandAliases());
    command.setPermission(ICommandClass.getCommandPermission());
    command.setDescription(ICommandClass.getCommandDescription());
    command.setUsage(ICommandClass.getUsage());

    command.setExecutor(ICommandClass);
    command.setTabCompleter(ICommandClass);

    Main.getInstance().getServer().getCommandMap().register("minecraft", command);
    Main.log(Level.INFO, "Registered " + command.getName() + " command.");
  }

  private static PluginCommand getCommand(String name) {
    PluginCommand command;

    try {
      Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      c.setAccessible(true);

      command = c.newInstance(name, Main.getInstance());
      return command;

    } catch (SecurityException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException |
             IllegalAccessException | InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  public static String generateCommandBasePermission(IModule module, ICommandClass ICommandClass) {
    return "coreplugin." + module.getId() + ".command.player." + ICommandClass.getCommandName() + ".use";
  }
}
