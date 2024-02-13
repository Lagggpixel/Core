/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.utils.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class UserDataSerializer {

  private final static Gson gson = new GsonBuilder()
      .setPrettyPrinting()
      .excludeFieldsWithoutExposeAnnotation()
      .registerTypeAdapterFactory(new LocationSerializer.LocationTypeAdapterFactory())
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
        ExceptionUtils.handleException(e);
      }
    }

    return map;
  }

  public static void saveData(Map<UUID, User> map) {
    map.forEach((k, v) -> {
      try (FileWriter writer = new FileWriter(parentFolder + "/" + k + ".json")) {
        gson.toJson(v, writer);
      } catch (IOException e) {
        ExceptionUtils.handleException(e);
      }
    });
  }
}
