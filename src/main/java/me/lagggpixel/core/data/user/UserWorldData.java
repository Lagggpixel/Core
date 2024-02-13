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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.serializers.LocationSerializer;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

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
  @JsonAdapter(LocationSerializer.LocationTypeAdapterFactory.class)
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
}
