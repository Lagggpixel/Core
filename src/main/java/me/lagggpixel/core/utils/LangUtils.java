package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.Lang;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class LangUtils {
  
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void loadLangConfig() {
    File langFile = new File(Main.getInstance().getDataFolder(), "lang.yml");
    if (!langFile.exists()) {
      try {
        Main.getInstance().getDataFolder().mkdir();
        langFile.createNewFile();
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
