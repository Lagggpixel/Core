/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
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
