package me.lagggpixel.core.modules.skills;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.skills.handlers.SkillHandler;
import me.lagggpixel.core.modules.skills.hooks.placeholders.SkillsExpansion;
import me.lagggpixel.core.modules.skills.listeners.*;
import me.lagggpixel.core.utils.FileUtil;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SkillsModule implements IModule {

  private File dataFolder;

  @Getter
  private File skill_exp;

  @Getter
  private File skill_level_up;

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
    dataFolder = new File(Main.getInstance().getDataFolder(), "module_data/skills");
    skill_exp = new File(dataFolder, "skill_exp.yml");
    skill_level_up = new File(dataFolder, "skill_level_up.yml");

    initConfig();

    this.skillHandler = new SkillHandler(this);

    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      new SkillsExpansion().register();
    }
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
    new SkillExpGainListener(this);
    new SkillLevelUpListener(this);
  }

  private void initConfig() {
    if (!skill_exp.exists()) {
      String resourcePath = "module_data/skills/skill_exp.yml";
      FileUtil.copyToDefault(resourcePath);
    }

    if (!skill_level_up.exists()) {
      String resourcePath = "module_data/skills/skill_level_up.yml";
      FileUtil.copyToDefault(resourcePath);
    }
  }
  
}
