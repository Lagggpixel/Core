/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.bazaar.menu;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuHistory;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.GUIRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @since January 22, 2024
 */
public class DefaultMenuHistory implements MenuHistory {
  private final BazaarModule module;
  private final Map<UUID, Stack<GUI>> playerHistories = new HashMap<>();
  private final Set<UUID> refreshingPlayers = new HashSet<>();

  public DefaultMenuHistory(BazaarModule module) {
    this.module = module;
  }

  @Override
  public void addGui(Player player, GUI gui) {
    UUID uniqueId = player.getUniqueId();

    if (!playerHistories.containsKey(uniqueId)) {
      playerHistories.put(uniqueId, new Stack<>());
    }

    Stack<GUI> history = playerHistories.get(uniqueId);
    history.add(gui);
  }

  @Override
  public void clearHistory(Player player) {
    playerHistories.remove(player.getUniqueId());
  }

  @Override
  public void setHistory(Player player, Stack<GUI> history) {
    playerHistories.put(player.getUniqueId(), history);
  }

  @Override
  public Stack<GUI> getHistory(Player player) {
    return playerHistories.get(player.getUniqueId());
  }

  @Override
  public boolean openPrevious(Player player) {
    boolean hasPrevious = false;

    UUID uniqueId = player.getUniqueId();

    if (playerHistories.containsKey(uniqueId)) {
      Stack<GUI> history = playerHistories.get(uniqueId);

      if (history.size() > 1) {
        history.pop();
        GUI gui = history.pop();
        gui.open(player);
        hasPrevious = true;
      }
    }

    if (!hasPrevious) {
      player.closeInventory();
    }

    return hasPrevious;
  }

  @Override
  public Optional<GUI> getPrevious(Player player) {
    return getPrevious(player.getUniqueId());
  }

  @Override
  public Optional<GUI> getPrevious(UUID uniqueId) {
    Stack<GUI> list = playerHistories.getOrDefault(uniqueId, new Stack<>());
    if (list.size() < 2) return Optional.empty();
    return Optional.of(list.get(list.size() - 2));
  }

  @Override
  public Optional<GUI> getCurrent(Player player) {
    return getCurrent(player.getUniqueId());
  }

  @Override
  public Optional<GUI> getCurrent(UUID uniqueId) {
    Stack<GUI> list = playerHistories.getOrDefault(uniqueId, new Stack<>());
    if (list.isEmpty()) return Optional.empty();
    return Optional.of(list.get(list.size() - 1));
  }

  @Override
  public void refreshGui(Player player) {
    UUID playerUniqueId = player.getUniqueId();
    if (refreshingPlayers.contains(playerUniqueId)) return;

    refreshingPlayers.add(playerUniqueId);
    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      GUIRepository.reopenCurrent(player);
      refreshingPlayers.remove(playerUniqueId);
    }, 5);
  }
}
