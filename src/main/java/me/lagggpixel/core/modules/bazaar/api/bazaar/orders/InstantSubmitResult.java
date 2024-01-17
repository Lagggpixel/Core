/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

public enum InstantSubmitResult {
    SUCCESS,
    NOT_ENOUGH,
    NOT_ENOUGH_STOCK,
    ERROR;

    public String getMessageId(OrderType type) {
        return "instant." + type.name().toLowerCase() + "." + name().toLowerCase().replace("_", "-");
    }
}
