/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
@RequiredArgsConstructor
@Getter
public class UpdateContext {
  private final GUI gui;
  private final Player player;

  public void repeat() {
    gui.update(player);
  }
}
