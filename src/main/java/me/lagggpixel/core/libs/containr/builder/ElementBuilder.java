/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.builder;

import me.lagggpixel.core.libs.containr.Element;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public interface ElementBuilder<T extends Element> {

  T build();

}
