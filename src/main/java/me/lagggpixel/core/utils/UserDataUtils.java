package me.lagggpixel.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserDataUtils {

  private final static Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

  private static final File parentFolder = new File(Main.getInstance().getDataFolder(), "data/users");

  @NotNull
  public static Map<UUID, User> loadData() {
    if (!parentFolder.exists()) {
      if (!parentFolder.mkdirs()) {
        Main.getInstance().getLogger().log(Level.SEVERE, "Failed to create parent folder: " + parentFolder.getAbsolutePath());
        return new ConcurrentHashMap<>();
      }
    }

    final var map = new ConcurrentHashMap<UUID, User>();
    final var files = parentFolder.listFiles();

    if (files == null) {
      return new ConcurrentHashMap<>();
    }

    for (File file : files) {
      final var name = file.getName().split("\\.")[0];

      try (FileReader reader = new FileReader(file)) {
        User loadedUser = gson.fromJson(reader, User.class);
        if (loadedUser != null) {
          map.put(loadedUser.getPlayerUUID(), loadedUser);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return map;
  }

  public static void saveData(Map<UUID, User> map) {
    map.forEach((k, v) -> {
      try (FileWriter writer = new FileWriter(parentFolder + "/" + k + ".json")) {
        gson.toJson(v, writer);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
  
  @Deprecated
  @NotNull
  public static Map<UUID, User> loadDataOld() {
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

  @Deprecated
  public static void saveDataOld(Map<UUID, User> map) {
    for (User value : map.values()) {
      setData(value);
    }
  }

  @Deprecated
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

  @Deprecated
  public static FileConfiguration getPlayerConfig(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }

  @Deprecated
  public static File getPlayerFile(UUID uuid) {
    if (!parentFolder.exists()) {
      parentFolder.mkdirs();
    }
    
    return new File(parentFolder, uuid.toString() + ".yml");
  }
}
