package me.lagggpixel.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserDataUtils {

  private final static Gson gson = new GsonBuilder()
      .setPrettyPrinting()
      .excludeFieldsWithoutExposeAnnotation()
      .registerTypeAdapterFactory(new Home.LocationTypeAdapterFactory())
      .registerTypeAdapterFactory(new InstantPlayerData.ItemStackArrayTypeAdapterFactory())
      .create();

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
}
