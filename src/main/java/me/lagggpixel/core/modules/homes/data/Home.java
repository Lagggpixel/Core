package me.lagggpixel.core.modules.homes.data;

import org.bukkit.Location;

public class Home {
    private String name;
    private Location location;

    /**
     * Constructs a new home.
     *
     * @param name     The name of the home.
     * @param location The location of the home.
     */
    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    /**
     * Gets the name of the home.
     *
     * @return The name of the home.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the location of the home.
     *
     * @return The location of the home.
     */
    public Location getLocation() {
        return location;
    }

    // You can add more methods or properties as needed
}
