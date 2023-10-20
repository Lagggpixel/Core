package me.lagggpixel.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HookUtils {
  
  public static void unloadPlugin(Plugin plugin) {
    String name = plugin.getName();
    PluginManager pluginManager = Bukkit.getPluginManager();
    SimpleCommandMap commandMap;
    List<Plugin> plugins;
    Map<String, Plugin> names;
    Map<String, Command> commands;
    Map<Event, SortedSet<RegisteredListener>> listeners = null;
    
    boolean reloadListeners = true;
    pluginManager.disablePlugin(plugin);
    
    try {
      Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
      pluginsField.setAccessible(true);
      plugins = (List<Plugin>) pluginsField.get(pluginManager);
      
      Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
      lookupNamesField.setAccessible(true);
      names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
      
      try {
        Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
        listenersField.setAccessible(true);
        listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
      } catch (Exception e) {
        reloadListeners = false;
      }
      
      Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
      commandMapField.setAccessible(true);
      commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
      
      Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommandsField.setAccessible(true);
      commands = (Map<String, Command>) knownCommandsField.get(commandMap);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    
    pluginManager.disablePlugin(plugin);
    
    if (plugins != null && plugins.contains(plugin))
      plugins.remove(plugin);
    
    if (names != null)
      names.remove(name);
    
    if (listeners != null) {
      for (SortedSet<RegisteredListener> set : listeners.values()) {
        set.removeIf(value -> value.getPlugin() == plugin);
      }
    }
    
    if (commandMap != null) {
      assert commands != null;
      for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry<String, Command> entry = it.next();
        if (entry.getValue() instanceof PluginCommand c) {
          if (c.getPlugin() == plugin) {
            c.unregister(commandMap);
            it.remove();
          }
        }
      }
    }
    
    ClassLoader cl = plugin.getClass().getClassLoader();
    if (cl instanceof URLClassLoader) {
      try {
        ((URLClassLoader) cl).close();
      } catch (IOException ex) {
        Logger.getLogger(HookUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    System.gc();
  }
  
  /**
   * Check whether the given plugin is installed and enabled
   *
   * @param pluginName The plugin name to check
   * @return Whether the plugin is installed and enabled
   */
  public static boolean checkIfPluginEnabled(String pluginName) {
    Plugin plugin = getPlugin(pluginName);
    if (plugin == null) {
      return false;
    } else return plugin.isEnabled();
  }
  
  public static boolean pluginHookIsEnabled(String pluginName) {
    return checkIfPluginEnabled(pluginName);
  }
  
  public static @Nullable Plugin getPlugin(String pluginName) {
    for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
      if (plugin.getName().equalsIgnoreCase(pluginName)) return plugin;
    return null;
  }
}
