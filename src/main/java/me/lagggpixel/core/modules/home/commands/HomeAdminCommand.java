package me.lagggpixel.core.modules.home.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.home.HomeModule;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.home.handlers.HomeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class HomeAdminCommand implements ICommandClass {

  private final HomeModule homeModule;
  private final HomeHandler homeHandler;

  public HomeAdminCommand(HomeModule homeModule, HomeHandler homeHandler) {
    this.homeModule = homeModule;
    this.homeHandler = homeHandler;
  }

  @Override
  public String getCommandName() {
    return "homeadmin";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of("homesadmin");
  }

  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(homeModule, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    String subCommand = args[0].toLowerCase();

    switch (subCommand) {
      case "create":
        handleAdminCreate(sender, args);
        break;
      case "delete":
        handleAdminDelete(sender, args);
        break;
      case "view":
        handleAdminView(sender, args);
        break;
      case "tpto":
        handleAdminTpTo(sender, args);
        break;
      default:
        sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
    }

    return true;
  }

  private void handleAdminCreate(CommandSender commandSender, String[] args) {

    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    // TODO - add permission to check if the sender has the permission to create a home

    if (args.length != 3) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    String targetPlayerName = args[1];
    String homeName = args[2];

    OfflinePlayer targetPlayer = sender.getServer().getPlayer(targetPlayerName);
    if (targetPlayer == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }
    User targetUser = Main.getUser(targetPlayer.getUniqueId());
    if (targetUser == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }

    if (homeHandler.homeNameInvalid(homeName)) {
      sender.sendMessage(Lang.HOME_NAME_INVALID.toComponentWithPrefix());
      return;
    }

    if (targetUser.getHomes().containsKey(homeName)) {
      sender.sendMessage(Lang.HOME_ALREADY_EXIST.toComponentWithPrefix(Map.of(
          "%home%", homeName
      )));
      return;
    }

    Home home = homeHandler.createHomeObject(homeName, sender.getLocation());
    homeHandler.setHome(targetUser, homeName, home);

    sender.sendMessage(Lang.HOME_CREATED_OTHER_PLAYER.toComponentWithPrefix(Map.of(
        "%home%", homeName,
        "%player%", targetPlayer.getName()
    )));
  }


  private void handleAdminDelete(CommandSender commandSender, String[] args) {
    // TODO - add permission to check if the sender has the permission to delete a home
    if (args.length != 3) {
      commandSender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    String targetPlayerName = args[1];
    String homeName = args[2];

    OfflinePlayer targetPlayer = commandSender.getServer().getPlayer(targetPlayerName);
    if (targetPlayer == null) {
      commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }
    User targetUser = Main.getUser(targetPlayer.getUniqueId());
    if (targetUser == null) {
      commandSender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }

    if (!targetUser.getHomes().containsKey(homeName)) {
      commandSender.sendMessage(Lang.HOME_DOES_NOT_EXIST.toComponentWithPrefix(Map.of(
          "%home%", homeName,
          "%player%", targetPlayer.getName()
      )));
      return;
    }

    homeHandler.deleteHome(targetUser, homeName);

    commandSender.sendMessage(Lang.HOME_DELETED_OTHER_PLAYER.toComponentWithPrefix(Map.of(
        "%home%", homeName,
        "%player%", targetPlayer.getName()
    )));
  }


  private void handleAdminView(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    // TODO - add permission to check if the sender has the permission to view homes

    if (args.length != 2) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    String targetPlayerName = args[1];

    OfflinePlayer targetPlayer = sender.getServer().getPlayer(targetPlayerName);
    if (targetPlayer == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }
    User targetUser = Main.getUser(targetPlayer.getUniqueId());
    if (targetUser == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }

    if (targetUser.getHomes().isEmpty()) {
      sender.sendMessage(Lang.HOME_NO_HOMES_OTHER_PLAYER.toComponentWithPrefix(Map.of(
          "%player%", targetPlayer.getName()
      )));
      return;
    }

    homeHandler.openHomesGUIOther(sender, targetUser);
  }


  private void handleAdminTpTo(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    // TODO - add permission to check if the sender has the permission to teleport to a home

    if (args.length != 3) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    String targetPlayerName = args[1];
    String homeName = args[2];

    OfflinePlayer targetPlayer = sender.getServer().getPlayer(targetPlayerName);
    if (targetPlayer == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }
    User targetUser = Main.getUser(targetPlayer.getUniqueId());
    if (targetUser == null) {
      sender.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix());
      return;
    }

    if (!targetUser.getHomes().containsKey(homeName)) {
      sender.sendMessage(Lang.HOME_DOES_NOT_EXIST.toComponentWithPrefix(Map.of(
          "%home%", homeName,
          "%player%", targetPlayer.getName()
      )));
      return;
    }

    Home targetHome = targetUser.getHomes().get(homeName);
    homeHandler.teleportToHome(sender, targetHome);

    sender.sendMessage(Lang.HOME_TELEPORTED_TO_HOME_OTHER_PLAYER.toComponentWithPrefix(Map.of(
        "%home%", homeName,
        "%player%", targetPlayer.getName()
    )));
  }


  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
