/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public enum SubmitResult {
    SUCCESS,
    NOT_ENOUGH,
    ERROR;

    public String getMessageId(OrderType type) {
        return "order." + type.name().toLowerCase() + "." + name().toLowerCase().replace("_", "-");
    }
}
