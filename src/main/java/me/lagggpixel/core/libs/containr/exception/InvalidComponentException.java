/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.exception;

import lombok.Getter;
import me.lagggpixel.core.libs.containr.Component;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class InvalidComponentException extends RuntimeException {

    @Getter
    private final Component component;

    public InvalidComponentException(Component component) {
        super("Component " + component.getClass().getName() + " is not valid!");
        this.component = component;
    }

}
