package me.lagggpixel.core.modules.skipnight.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.skipnight.managers.SkipNightVoteManager;
import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class SkipNightCommand extends CommandClass {
  
  SkipNightModule module;
  SkipNightVoteManager skipNightVoteManager;
  
  public SkipNightCommand(SkipNightModule module, SkipNightVoteManager skipNightVoteManager) {
    this.module = module;
    this.skipNightVoteManager = skipNightVoteManager;
  }
  
  @Override
  public String getCommandName() {
    return "skipnight";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("sn", "skipnight");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(module, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("yes")) {
        if (!(sender instanceof Player)) {
          getLogger().info("Vote not allowed from console.");
        } else {
          this.skipNightVoteManager.addYes(((Player) sender).getUniqueId(), SkipNightVoteType.NIGHT);
        }
      } else if (args[0].equalsIgnoreCase("no")) {
        if (!(sender instanceof Player)) {
          getLogger().info("Vote not allowed from console.");
        } else {
          this.skipNightVoteManager.addNo(((Player) sender).getUniqueId(), SkipNightVoteType.NIGHT);
        }
      }
    } else {
      if (!(sender instanceof Player)) {
        getLogger().info("Vote can't be started from console.");
      } else {
        this.skipNightVoteManager.start((Player) sender, SkipNightVoteType.NIGHT);
      }
    }
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
