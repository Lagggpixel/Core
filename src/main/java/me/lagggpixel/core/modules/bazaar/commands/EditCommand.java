/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.bazaar.commands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ICommandClass;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.messageinput.MessageInputManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @since January 22, 2024
 */
public class EditCommand implements ICommandClass {
  private final BazaarModule module;

  public EditCommand(BazaarModule module) {
    this.module = module;
  }

  @Override
  public String getCommandName() {
    return "bazaaredit";
  }

  @Override
  public String getCommandDescription() {
    return null;
  }

  @Override
  public List<String> getCommandAliases() {
    return List.of("bazaaredit", "bze");
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
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return false;
    }

    if (args.length == 0) {
      module.getBazaar().openEdit(player, module.getBazaar().getCategories().get(0));
      return true;
    }

    if (args.length >= 2 && args[0].equals("lore")) {
      MessageInputManager messageInputManager = module.getMessageInputManager();
      int lineIndex = args.length >= 3 ? Integer.parseInt(args[2]) : -1;
      switch (args[1]) {
        case "remove":
          if (lineIndex < 0) break;
          messageInputManager.removeLine(player, lineIndex);
          break;
        case "edit":
          if (lineIndex < 0) break;
          messageInputManager.editLine(player, lineIndex);
          break;
        case "add":
          messageInputManager.addLine(player);
          break;
        case "confirm":
          messageInputManager.confirmMultiLineInput(player);
          break;
      }
    }

    //bazaaredit lore edit/remove/add/confirm lineindex

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
