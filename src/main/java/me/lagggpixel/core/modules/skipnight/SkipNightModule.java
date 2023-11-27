package me.lagggpixel.core.modules.skipnight;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.skipnight.commands.SkipNightCommand;
import me.lagggpixel.core.modules.skipnight.managers.SkipNightVoteManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class SkipNightModule extends Module {

  public SkipNightVoteManager skipNightVoteManager;
  @Getter
  private final String SKIP_NIGHT_PERMISSION = "coreplugin.skipnight.command.player.skipnight.use";

  @NotNull
  @Override
  public String getId() {
    return "skipnight";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    skipNightVoteManager = new SkipNightVoteManager(this);
    CommandUtils.registerCommand(new SkipNightCommand(this, skipNightVoteManager));
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {

  }

  @Override
  public void registerListeners() {
    Main.getPluginManager().registerEvents(skipNightVoteManager, Main.getInstance());
  }
}
