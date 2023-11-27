package me.lagggpixel.core.modules.guilds.data.loadsave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.modules.guilds.data.Guild;

import java.io.*;

public class GuildGsonSerializer {
  
  public static void saveGuild(Guild guild, File file) {
    try (Writer writer = new FileWriter(file)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(guild, writer);
      System.out.println("Guild saved successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static Guild loadGuild(File file) {
    try (Reader reader = new FileReader(file)) {
      Gson gson = new Gson();
      return gson.fromJson(reader, Guild.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
