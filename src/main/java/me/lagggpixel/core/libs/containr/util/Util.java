/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.util;

import me.lagggpixel.core.libs.containr.Container;
import me.lagggpixel.core.libs.containr.geometry.Region;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public final class Util {

    public static int[] relativeToRealCoords(int[] relative, Container container) {
        Region region = container.getSelection();
        int cornerX = region.xMin();
        int cornerY = region.yMin();
        return new int[] {relative[0] + cornerX, relative[1] + cornerY};
    }

    public static int[] pos(int index, int containerXSize) {
        int row = (int) Math.floor(((double) index) / ((double) containerXSize));
        int column = index % containerXSize;
        return new int[] {column, row};
    }

    public static int pos(int[] loc, int containerXSize) {
        if(loc.length < 2) throw new IndexOutOfBoundsException("Index does not have enough length. Expected 2, not " + loc.length + ".");
        return (loc[1] * containerXSize) + loc[0];
    }

    public static void runCatching(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
