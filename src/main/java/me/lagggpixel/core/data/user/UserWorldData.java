/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.data.user;

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
import lombok.Setter;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.serializers.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Lagggpixel
 * @since February 06, 2024
 */
@Data
@Getter
@Setter
public final class UserWorldData {
  @SerializedName("WorldName")
  @Expose
  private final @NotNull String name;
  @SerializedName("Location")
  @Expose
  @JsonAdapter(LocationTypeAdapterFactory.class)
  private @NotNull Location location;

  public UserWorldData(@NotNull String name,
                       @NotNull Location location) {
    this.name = name;
    this.location = location;
  }

  @SerializedName("WorldName")
  public @NotNull String name() {
    return name;
  }

  @SerializedName("Location")
  public @NotNull Location location() {
    return location;
  }

  public void modifyLocation(Location location) {
    this.location = location;
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
