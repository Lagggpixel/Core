/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

/**
 * @since January 22, 2024
 */
public enum InstantSubmitResult {
  SUCCESS,
  NOT_ENOUGH,
  NOT_ENOUGH_STOCK,
  ERROR;

  public String getMessageId(OrderType type) {
    return "instant." + type.name().toLowerCase() + "." + name().toLowerCase().replace("_", "-");
  }
}
