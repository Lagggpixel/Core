/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

/**
 * @since January 22, 2024
 */
public interface MenuHistory {
  void addGui(Player player, GUI gui);

  void clearHistory(Player player);

  void setHistory(Player player, Stack<GUI> history);

  Stack<GUI> getHistory(Player player);

  boolean openPrevious(Player player);

  Optional<GUI> getPrevious(Player player);

  Optional<GUI> getPrevious(UUID uniqueId);

  Optional<GUI> getCurrent(Player player);

  Optional<GUI> getCurrent(UUID uniqueId);

  void refreshGui(Player player);
}
