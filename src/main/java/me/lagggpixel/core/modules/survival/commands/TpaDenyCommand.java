package me.lagggpixel.core.modules.survival.commands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.survival.SurvivalModule;
import me.lagggpixel.core.modules.survival.handlers.TpaHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class TpaDenyCommand implements ICommandClass {
  
  private final SurvivalModule module;
  private final TpaHandler tpaHandler;
  
  public TpaDenyCommand(SurvivalModule module, TpaHandler tpaHandler) {
    this.module = module;
    this.tpaHandler = tpaHandler;
  }
  
  @Override
  public String getCommandName() {
    return "tpadeny";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("tpadeny", "tpadeny", "teleportdeny", "teleportaskdeny");
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
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return true;
    }
    
    if (strings.length != 1) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    Player target = Bukkit.getPlayer(strings[0]);
    if (target == null || !tpaHandler.getTpaRequestMap().containsKey(target) || !tpaHandler.getTpaRequestMap().get(target).getTarget().equals(sender)) {
      sender.sendMessage(Lang.TPA_REQUEST_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", strings[0])));
      return true;
    }
    
    tpaHandler.getTpaRequestMap().get(target).denyTpa();
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}