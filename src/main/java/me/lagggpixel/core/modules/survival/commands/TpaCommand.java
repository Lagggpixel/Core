package me.lagggpixel.core.modules.survival.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.survival.data.TpaRequest;
import me.lagggpixel.core.data.User;
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

public class TpaCommand implements ICommandClass {
  
  private final SurvivalModule module;
  private final TpaHandler tpaHandler;
  
  public TpaCommand(SurvivalModule module, TpaHandler tpaHandler) {
    this.module = module;
    this.tpaHandler = tpaHandler;
  }
  
  @Override
  public String getCommandName() {
    return "teleportrequest";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("tpr", "tpa", "tprequest", "tpask", "teleportask", "teleportrequest");
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
    User user = Main.getUser(sender);
    if (strings.length != 1) {
      user.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    if (tpaHandler.getTpaRequestMap().containsKey(sender)) {
      user.sendMessage(Lang.TPA_REQUEST_ALREADY_OUTGOING.toComponentWithPrefix());
      return true;
    }
    
    Player target = Bukkit.getPlayer(strings[0]);
    if (target == null) {
      user.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return true;
    }
    
    new TpaRequest(sender, target);
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
