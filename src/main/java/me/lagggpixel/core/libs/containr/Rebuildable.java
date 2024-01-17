/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a rebuildable GUI.
 * This interface is determined to be used ONLY by implementing
 * in a GUI class since only GUI class handles it.
 * <pre>
 *     public class MyGUI extends GUI implements Rebuildable {
 *      public void rebuild() {
 *          // ...
 *      }
 *     }
 * </pre>
 *
 * @author ZorTik
 * @deprecated Deprecated in favor of {@link Container#refresh(Player)}
 */
@Deprecated
public interface Rebuildable {

    /**
     * Rebuilds the GUI.
     *
     * @deprecated Use {@link #rebuild(Player)} instead.
     */
    @ApiStatus.ScheduledForRemoval
    @Deprecated
    default void rebuild() {}

    /**
     * Rebuilds the GUI.
     *
     * @param player The player for whom the GUI is being rebuilt.
     */
    default void rebuild(Player player) {}

}
