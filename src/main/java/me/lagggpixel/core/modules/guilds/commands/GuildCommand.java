package me.lagggpixel.core.modules.guilds.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuildCommand extends CommandClass {
  
  GuildModule guildModule;
  
  public GuildCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public String getCommandName() {
    return "guild";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("guilds", "guild", "g");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(guildModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return false;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
