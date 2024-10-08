/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.internal.util;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
@FunctionalInterface
public interface QuadConsumer<T, U, D, A> {

  void accept(T o1, U o2, D o3, A o4);

}
