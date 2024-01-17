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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Data;
import lombok.Getter;
import me.lagggpixel.core.serializers.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Getter
public final class Home implements ConfigurationSerializable {
  @SerializedName("Name")
  @Expose
  private final String name;
  @SerializedName("Location")
  @Expose
  @JsonAdapter(Home.LocationTypeAdapterFactory.class)
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
  
  @Override
  public @NotNull Map<String, Object> serialize() {
    return new HashMap<>() {{
      put("name", name);
      put("location", location);
    }};
  }

  public static class LocationTypeAdapter extends TypeAdapter<Location> {

    @Override
    public void write(JsonWriter out, Location value) throws IOException {
      out.value(LocationSerializer.serializeLocation(value));
    }

    @Override
    public Location read(JsonReader in) throws IOException {
      String locationString = in.nextString();
      return LocationSerializer.deserializeLocation(locationString);
    }
  }

  public static class LocationTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      if (type.getRawType() == ItemStack[].class) {
        return (TypeAdapter<T>) new Home.LocationTypeAdapter();
      }
      return null;
    }
  }
}
