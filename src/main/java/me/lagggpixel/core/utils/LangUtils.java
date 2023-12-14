package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class LangUtils {
  
  public static void loadLangConfig() {
    File langFile = new File(Main.getInstance().getDataFolder(), "lang.yml");
    if (!langFile.exists()) {
      try {
        if (!Main.getInstance().getDataFolder().mkdir()) {
          throw new RuntimeException("Could not create data folder for core-plugin.");
        }
        if (!langFile.createNewFile()) {
          throw new RuntimeException("Could not create language file for core-plugin");
        }
        InputStream defConfigStream = Main.getInstance().getResource("lang.yml");
        if (defConfigStream != null) {
          YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
          defConfig.save(langFile);
          Lang.setFile(defConfig);
        }
      } catch (IOException e) {
        Main.log(Level.SEVERE, "Couldn't create language file.");
        Main.log(Level.SEVERE, "This is a fatal error. Now disabling");
        Main.log(Level.SEVERE, e.getMessage());
        Main.getInstance().onDisable();
      }
    }
    YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
    for (Lang item : Lang.values()) {
      if (conf.getString(item.getPath()) == null) {
        conf.set(item.getPath(), item.getDefault());
      }
    }
    Lang.setFile(conf);
    try {
      conf.save(langFile);
    } catch (IOException e) {
      Main.log(Level.WARNING, "Failed to save lang.yml.");
      Main.log(Level.WARNING, "Report this stack trace to the plugin creator.");
      Main.log(Level.SEVERE, e.getMessage());
    }
  }
}
