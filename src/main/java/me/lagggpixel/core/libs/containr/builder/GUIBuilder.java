/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.builder;

import me.lagggpixel.core.libs.containr.GUI;

/**
 * A GUI builder.
 *
 * @param <T> The type of the GUI.
 * @author ZorTik
 * @since January 22, 2024
 */
public interface GUIBuilder<T extends GUI> {

  T build();

}
