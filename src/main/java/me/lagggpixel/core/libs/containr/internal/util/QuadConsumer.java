/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.internal.util;

@FunctionalInterface
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface QuadConsumer<T, U, D, A> {

    void accept(T o1, U o2, D o3, A o4);

}
