/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.bazaar.messageinput;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

/**
 * @since January 22, 2024
 */
public class MessageInputManager {
  private final BazaarModule module;
  private final Map<UUID, Consumer<String>> playerOneLineInputCallbacks = new HashMap<>();
  private final Map<UUID, Consumer<List<String>>> playerMultiLineInputCallbacks = new HashMap<>();
  private final Map<UUID, List<String>> playerMultiLineInputValues = new HashMap<>();

  public MessageInputManager(BazaarModule module) {
    this.module = module;

    Bukkit.getPluginManager().registerEvents(new MessageInputListeners(this), Main.getInstance());
  }

  public void handleInput(Player player, String line) {
    playerOneLineInputCallbacks.getOrDefault(player.getUniqueId(), unused -> {
    }).accept(line);
    playerOneLineInputCallbacks.remove(player.getUniqueId());
  }

  public void requirePlayerMessageInput(Player player, Consumer<String> callback) {
    playerOneLineInputCallbacks.put(player.getUniqueId(), callback);
  }

  public void removeLine(Player player, int lineIndex) {
    playerMultiLineInputValues.get(player.getUniqueId()).remove(lineIndex);
    sendMultiLineEditMessage(player);
  }

  public void editLine(Player player, int lineIndex) {
    player.sendMessage(ChatColor.GRAY + "Enter new content of this line:");
    requirePlayerMessageInput(player, line -> {
      playerMultiLineInputValues.get(player.getUniqueId()).set(lineIndex, line);
      sendMultiLineEditMessage(player);
    });
  }

  public void addLine(Player player) {
    player.sendMessage(ChatColor.GRAY + "Enter content of new line:");
    requirePlayerMessageInput(player, line -> {
      playerMultiLineInputValues.get(player.getUniqueId()).add(line);
      sendMultiLineEditMessage(player);
    });
  }

  public void confirmMultiLineInput(Player player) {
    handleInput(player, playerMultiLineInputValues.get(player.getUniqueId()));
  }

  public void handleInput(Player player, List<String> lines) {
    playerMultiLineInputCallbacks.getOrDefault(player.getUniqueId(), unused -> {
    }).accept(lines);
    removePlayer(player);
  }

  public void requirePlayerMessageInputMultiLine(Player player, List<String> defaultLines, Consumer<List<String>> callback) {
    playerMultiLineInputValues.put(player.getUniqueId(), defaultLines == null ? new ArrayList<>() : defaultLines);
    playerMultiLineInputCallbacks.put(player.getUniqueId(), callback);
    sendMultiLineEditMessage(player);
  }

  private void sendMultiLineEditMessage(Player player) {
    List<String> lines = playerMultiLineInputValues.get(player.getUniqueId());

    player.sendMessage("");

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      player.spigot().sendMessage(new TextComponent(line + " "),
          Utils.createClickableText(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "]", "bazaaredit lore remove " + i),
          new TextComponent(" "),
          Utils.createClickableText(ChatColor.GRAY + "[" + ChatColor.AQUA + "✎" + ChatColor.GRAY + "]", "bazaaredit lore edit " + i));
    }

    player.sendMessage("");

    player.spigot().sendMessage(Utils.createClickableText(ChatColor.GREEN + "Add Line", "bazaaredit lore add"),
        new TextComponent(" | "),
        Utils.createClickableText(ChatColor.GREEN + "Confirm", "bazaaredit lore confirm"));
  }

  public boolean isWaitingForOneLineInput(Player player) {
    return playerOneLineInputCallbacks.containsKey(player.getUniqueId());
  }

  public void removePlayer(Player player) {
    UUID uniqueId = player.getUniqueId();
    playerOneLineInputCallbacks.remove(uniqueId);
    playerMultiLineInputCallbacks.remove(uniqueId);
    playerMultiLineInputValues.remove(uniqueId);
  }
}
