package me.lagggpixel.core.modules.merchant.commands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.modules.merchant.commands.subCommands.MerchantCreateSubCommand;
import me.lagggpixel.core.modules.merchant.commands.subCommands.MerchantRenameSubCommand;
import me.lagggpixel.core.modules.merchant.commands.subCommands.MerchantSelectSubCommand;
import me.lagggpixel.core.modules.merchant.commands.subCommands.MerchantTphereSubCommand;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantCommand implements ICommandClass {
  
  private final MerchantModule merchantModule;
  private final Map<String, ISubCommand> subCommands;
  
  public MerchantCommand(MerchantModule merchantModule) {
    this.merchantModule = merchantModule;
    this.subCommands = new HashMap<>();
    
    subCommands.put("create", new MerchantCreateSubCommand(merchantModule));
    subCommands.put("select", new MerchantSelectSubCommand(merchantModule));
    subCommands.put("tphere", new MerchantTphereSubCommand(merchantModule));
    subCommands.put("rename", new MerchantRenameSubCommand(merchantModule));
  }
  
  
  @Override
  public String getCommandName() {
    return "merchant";
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
    return CommandUtils.generateCommandBasePermission(merchantModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (args.length == 0) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }
    
    String subCommand = args[0].toLowerCase();
    
    if (subCommands.containsKey(subCommand)) {
      subCommands.get(subCommand).execute(commandSender, args);
      return true;
    }
    
    commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
    if (args.length == 1) {
      return subCommands.keySet().stream().toList();
    }
    return null;
  }
}
