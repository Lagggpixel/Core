package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class CommandUtils {

    public static void registerCommand(CommandClass commandClass) {
        PluginCommand command = getCommand(commandClass.getCommandName());

        command.setAliases(commandClass.getCommandAliases());
        command.setPermission(commandClass.getCommandPermission());
        command.setDescription(commandClass.getCommandDescription());
        command.setUsage(commandClass.getUsage());

        command.setExecutor(commandClass);
        command.setTabCompleter(commandClass);

        Main.getInstance().getServer().getCommandMap().register("minecraft", command);
        Main.getInstance().getServer().getLogger().log(Level.INFO,  "[" + Main.getInstance().getName() + "] Registered " + command.getName() + " command.");
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
            e.printStackTrace();
            return null;
        }
    }
}
