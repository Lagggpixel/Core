package me.lagggpixel.core.modules.guilds.commands;

import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.subCommands.*;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildCommand implements ICommandClass {

  private final GuildModule guildModule;
  private final Map<String, ISubCommand> subCommands;

  public GuildCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
    this.subCommands = new HashMap<>();

    subCommands.put("create", new GuildCreateCommand(guildModule));
    subCommands.put("invite", new GuildInviteCommand(guildModule));
    subCommands.put("claim", new GuildClaimCommand(guildModule));
    subCommands.put("disband", new GuildDisbandCommand(guildModule));
    subCommands.put("kick", new GuildKickCommand(guildModule));
    subCommands.put("help", new GuildHelpCommand(guildModule));
    subCommands.put("sethome", new GuildSetHomeCommand(guildModule));
    subCommands.put("join", new GuildJoinCommand(guildModule));
    subCommands.put("who", new GuildWhoCommand(guildModule));
    subCommands.put("map", new GuildMapCommand(guildModule));
    subCommands.put("deposit", new GuildDepositCommand(guildModule));
    subCommands.put("withdraw", new GuildWithdrawCommand(guildModule));
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
