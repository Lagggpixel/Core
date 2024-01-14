package me.lagggpixel.core.libs.containr.exception;

import lombok.Getter;
import me.lagggpixel.core.libs.containr.Component;

public class InvalidComponentException extends RuntimeException {

    @Getter
    private final Component component;

    public InvalidComponentException(Component component) {
        super("Component " + component.getClass().getName() + " is not valid!");
        this.component = component;
    }

}
