package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserUtils {
  
  private static final File parentFolder = new File(Main.getInstance().getDataFolder(), "data/users");
  
  @NotNull
  public static Map<UUID, User> loadData() {
    if (!parentFolder.exists()) {
      parentFolder.mkdirs();
    }
    
    final var map = new ConcurrentHashMap<UUID, User>();
    final var files = parentFolder.listFiles();
    
    if (files == null) {
      return new ConcurrentHashMap<>();
    }
    
    for (File file : files) {
      final var name = file.getName().split("\\.")[0];
      final UUID uuid;
      
      try {
        uuid = UUID.fromString(name);
      } catch (IllegalArgumentException e) {
        continue;
      }
      
      var data = getDataFromFile(file);
      if (data != null) map.put(uuid, data);
    }
    
    return map;
  }
  
  public static void saveData(Map<UUID, User> map) {
    for (User value : map.values()) {
      setData(value);
    }
  }
  
  @Nullable
  public static User getDataFromFile(File file) {
    try {
      return getPlayerConfig(file).getSerializable("user", User.class);
    } catch (Exception e) {
      return null;
    }
  }
  
  public static void setData(User data) {
    var file = getPlayerFile(data.getPlayerUUID());
    var config = YamlConfiguration.loadConfiguration(file);
    
    config.set("user", data);
    
    try {
      config.save(file);
    } catch (IOException e) {
      Main.getInstance().getLogger().log(Level.WARNING, "Unable to save player moderation data " + data, e);
    }
  }
  
  public static FileConfiguration getPlayerConfig(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }
  
  public static File getPlayerFile(UUID uuid) {
    if (!parentFolder.exists()) {
      parentFolder.mkdirs();
    }
    
    return new File(parentFolder, uuid.toString() + ".yml");
  }
}
