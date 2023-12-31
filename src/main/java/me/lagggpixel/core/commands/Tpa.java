package me.lagggpixel.core.commands;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.TpaRequest;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tpa {
  
  @Getter
  private static final Map<Player, TpaRequest> tpaRequestMap = new HashMap<>();
  
  public Tpa() {
    registerCommands();
  }
  
  private void registerCommands() {
    CommandUtils.registerCommand(new TpaCommand());
    CommandUtils.registerCommand(new TpaAcceptCommand());
    CommandUtils.registerCommand(new TpaDenyCommand());
    CommandUtils.registerCommand(new TpaCancelCommand());
  }
  
}

class TpaCommand implements ICommandClass {
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
    return CommandUtils.generateCommandBasePermission(null, this);
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
    
    if (Tpa.getTpaRequestMap().containsKey(sender)) {
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

class TpaCancelCommand implements ICommandClass {
  @Override
  public String getCommandName() {
    return "tpacancel";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("tpacancel", "tpcancel", "teleportcancel", "teleportaskcancel");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(null, this);
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
    
    if (!Tpa.getTpaRequestMap().containsKey(sender)) {
      sender.sendMessage(Lang.TPA_NO_REQUEST_OUTGOING.toComponentWithPrefix());
      return true;
    }
    
    Tpa.getTpaRequestMap().get(sender).cancelTpa();
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}

class TpaAcceptCommand implements ICommandClass {
  
  @Override
  public String getCommandName() {
    return "tpaccept";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("tpaccept", "tpaaccept", "teleportaccept", "teleportaskaccept");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(null, this);
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
    if (target == null || !Tpa.getTpaRequestMap().containsKey(target) || !Tpa.getTpaRequestMap().get(target).getTarget().equals(sender)) {
      sender.sendMessage(Lang.TPA_REQUEST_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", strings[0])));
      return true;
    }
    
    Tpa.getTpaRequestMap().get(target).acceptTpa();
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}

class TpaDenyCommand implements ICommandClass {
  
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
    return CommandUtils.generateCommandBasePermission(null, this);
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
    if (target == null || !Tpa.getTpaRequestMap().containsKey(target) || !Tpa.getTpaRequestMap().get(target).getTarget().equals(sender)) {
      sender.sendMessage(Lang.TPA_REQUEST_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", strings[0])));
      return true;
    }
    
    Tpa.getTpaRequestMap().get(target).denyTpa();
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}