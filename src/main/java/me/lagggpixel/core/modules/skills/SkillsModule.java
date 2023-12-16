package me.lagggpixel.core.modules.skills;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import me.lagggpixel.core.modules.skills.listeners.BlockBreakListener;
import me.lagggpixel.core.modules.skills.listeners.EntityDeathListener;
import me.lagggpixel.core.modules.skills.listeners.NaturalBlocksChangedListeners;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Level;

public class SkillsModule implements IModule {

  private final File dataFolder = new File(Main.getInstance().getDataFolder(), "data/modules/skills");
  @Getter
  private final File skill_exp = new File(dataFolder, "skill_exp");
  @Getter
  private final File skill_level_up = new File(dataFolder, "skill_level_up");

  @Getter
  private SkillHandler skillHandler;

  @NotNull
  @Override
  public String getId() {
    return "skill";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    initConfig();

    this.skillHandler = new SkillHandler(this);
  }

  @Override
  public void onDisable() {

  }

  @Override
  public void registerCommands() {

  }

  @Override
  public void registerListeners() {
    new BlockBreakListener(this);
    new NaturalBlocksChangedListeners(this);
    new EntityDeathListener(this);
  }

  private void initConfig() {
    boolean replace = false;

    if (!skill_exp.exists()) {
      String resourcePath = "module_data/skills/skill_exp.yml";

      copyToDefault(replace, resourcePath);
    }

    if (!skill_level_up.exists()) {
      String resourcePath = "module_data/skills/skill_level_up.yml";

      copyToDefault(replace, resourcePath);
    }

  }

  private void copyToDefault(boolean replace, String resourcePath) {
    resourcePath = resourcePath.replace('\\', '/');
    InputStream in = Main.getInstance().getResource(resourcePath);
    if (in == null) {
      throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
    } else {
      File outFile = new File(dataFolder, resourcePath);
      int lastIndex = resourcePath.lastIndexOf(47);
      File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));
      if (!outDir.exists()) {
        outDir.mkdirs();
      }

      try {
        if (outFile.exists() && !replace) {
          Main.log(Level.WARNING, "Could not save " + resourcePath + " to " + outFile + " because " + outFile.getName() + " already exists.");
        } else {
          OutputStream out = new FileOutputStream(outFile);
          byte[] buf = new byte[1024];

          int len;
          while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
          }

          out.close();
          in.close();
        }
      } catch (IOException var10) {
        Main.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile);
        var10.printStackTrace();
      }
    }
  }

}
