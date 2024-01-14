package me.lagggpixel.core.libs.containr.builder;

import me.lagggpixel.core.libs.containr.Element;

public interface ElementBuilder<T extends Element> {

    T build();

}
