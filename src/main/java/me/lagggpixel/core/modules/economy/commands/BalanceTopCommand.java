package me.lagggpixel.core.modules.economy.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BalanceTopCommand extends CommandClass {

  private final EconomyModule economyModule;
  private final EconomyManager economyManager;

  public BalanceTopCommand(EconomyModule economyModule, EconomyManager economyManager) {
    this.economyModule = economyModule;
    this.economyManager = economyManager;
  }

  @Override
  public String getCommandName() {
    return "balancetop";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of("balancetop", "baltop");
  }

  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(economyModule, this);
  }

  @Override
  public String getUsage() {
    return null;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    if (args.length > 0) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return true;
    }

    List<Map.Entry<UUID, Double>> balanceTop = economyManager.getBalanceTop();

    if (balanceTop.isEmpty()) {
      sender.sendMessage(Lang.ECONOMY_BALTOP_NO_PLAYER_FOUND.toComponentWithPrefix());
      return true;
    }

    sender.sendMessage(Lang.ECONOMY_BALTOP_HEADER.toComponent());

    for (int i = 0; i < Math.min(10, balanceTop.size()); i++) {
      Map.Entry<UUID, Double> entry = balanceTop.get(i);
      sender.sendMessage(Lang.ECONOMY_BALTOP_LISTING.toComponent(Map.of(
          "%position%", String.valueOf(i + 1),
          "%player%", Main.getUser(entry.getKey()).getPlayerName(),
          "%balance%", String.valueOf(entry.getValue())
      )));
      sender.sendMessage((i + 1) + ". " + entry.getKey() + ": " + entry.getValue());
    }

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}