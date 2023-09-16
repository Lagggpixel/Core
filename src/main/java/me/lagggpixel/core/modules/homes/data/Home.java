package me.lagggpixel.core.modules.homes.data;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Getter
public final class Home implements ConfigurationSerializable {
    public final String name;
    public final Location location;

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

    public String name() {
        return name;
    }

    public Location location() {
        return location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Home) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public String toString() {
        return "Home[" +
                "name=" + name + ", " +
                "location=" + location + ']';
    }

    public Home(Map<String, Object> map) {
        this.name = String.valueOf(map.get("name"));
        this.location = (Location) map.get("location");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return new HashMap<>() {{
            put("name", name);
            put("location", location);
        }};
    }
}
