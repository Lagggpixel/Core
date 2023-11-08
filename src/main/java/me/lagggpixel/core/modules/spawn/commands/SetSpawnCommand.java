package me.lagggpixel.core.modules.spawn.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.spawn.managers.SpawnManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetSpawnCommand extends CommandClass {

  private final SpawnModule spawnModule;
  private final SpawnManager spawnManager;

  public SetSpawnCommand(SpawnModule spawnModule, SpawnManager spawnManager) {
    this.spawnModule = spawnModule;
    this.spawnManager = spawnManager;
  }

  @Override
  public String getCommandName() {
    return "setspawn";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of();
  }

  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(spawnModule, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

    if (commandSender instanceof Player player) {
      spawnManager.setSpawnLocation(player.getLocation());
      spawnManager.saveSpawnLocation();

      commandSender.sendMessage("Spawn location set successfully!");
    } else {
      commandSender.sendMessage("Only players can set the spawn location.");
    }

    return true;


  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
