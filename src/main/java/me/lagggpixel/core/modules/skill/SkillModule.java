package me.lagggpixel.core.modules.skill;

import me.lagggpixel.core.interfaces.IModule;
import org.jetbrains.annotations.NotNull;

public class SkillModule implements IModule {
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
