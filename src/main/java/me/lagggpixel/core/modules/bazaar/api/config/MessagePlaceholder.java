/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.config;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public final class MessagePlaceholder implements Placeholder {
    private final String placeholder;
    private final String value;

    public MessagePlaceholder(String placeholder, String value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValue() {
        return value;
    }

    public String replace(String text) {
        return text.replaceAll("%" + placeholder + "%", value);
    }
}
