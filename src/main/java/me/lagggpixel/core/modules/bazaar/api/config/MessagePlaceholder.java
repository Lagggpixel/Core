/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.api.config;

import lombok.Getter;

/**
 * @since January 22, 2024
 */
@Getter
public record MessagePlaceholder(String placeholder, String value) implements Placeholder {

  public String replace(String text) {
    return text.replaceAll("%" + placeholder + "%", value);
  }
}
