package me.lagggpixel.core.modules.guilds.data.loadsave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.modules.guilds.data.Guild;

import java.io.*;

public class GuildGsonSerializer {
  
  public static void saveGuild(Guild guild, File file) {
    if (!file.exists()) {
      try {
        if (!file.getParentFile().exists()) {
          if (!file.getParentFile().mkdirs()) {
            throw new RuntimeException("Failed to create guild file folder: " + file.getParentFile().getPath());
          }
        }
        if (!file.createNewFile()) {
          throw new RuntimeException("Failed to create guild file: " + file.getName());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    try (Writer writer = new FileWriter(file)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(guild, writer);
      System.out.println("Guild saved successfully.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Guild loadGuild(File file) {
    if (!file.exists()) {
      throw new RuntimeException("File not found: " + file.getName());
    }
    try (Reader reader = new FileReader(file)) {
      Gson gson = new Gson();
      return gson.fromJson(reader, Guild.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
