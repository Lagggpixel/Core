package me.lagggpixel.core.modules.skill;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class SkillModule extends Module {
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

  }

  @Override
  public void onDisable() {

  }

  @Override
  public void registerCommands() {

  }

  @Override
  public void registerListeners() {

  }
}
