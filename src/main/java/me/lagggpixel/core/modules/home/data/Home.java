/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.home.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import me.lagggpixel.core.serializers.LocationSerializer;
import org.bukkit.Location;

import java.util.Map;
import java.util.Objects;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Data
@Getter
public final class Home {
  @SerializedName("Name")
  @Expose
  private final String name;
  @SerializedName("Location")
  @Expose
  @JsonAdapter(LocationSerializer.LocationTypeAdapterFactory.class)
  private final Location location;

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
}
