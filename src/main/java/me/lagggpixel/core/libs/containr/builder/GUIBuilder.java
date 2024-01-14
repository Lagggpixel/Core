package me.lagggpixel.core.libs.containr.builder;

import me.lagggpixel.core.libs.containr.GUI;

/**
 * A GUI builder.
 *
 * @param <T> The type of the GUI.
 * @author ZorTik
 */
public interface GUIBuilder<T extends GUI> {

    T build();

}
