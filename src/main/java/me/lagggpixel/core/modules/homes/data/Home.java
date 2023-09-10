package me.lagggpixel.core.modules.homes.data;

import org.bukkit.Location;

/**
 * @param name     -- GETTER --
 *                 Gets the name of the home.
 * @param location -- GETTER --
 *                 Gets the location of the home.
 */
public record Home(String name, Location location) {
    /**
     * Constructs a new home.
     *
     * @param name     The name of the home.
     * @param location The location of the home.
     */
    public Home {
    }

    // You can add more methods or properties as needed
}
