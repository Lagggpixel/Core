/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.handlers;

import lombok.Getter;
import me.lagggpixel.core.modules.survival.data.TpaRequest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


@Getter
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class TpaHandler {
  
  private final Map<Player, TpaRequest> tpaRequestMap = new HashMap<>();
  
}