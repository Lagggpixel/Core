/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.serializers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class LocationSerializer {
  /**
   * Serializes a Location object into a String representation.
   *
   * @param  l  the Location object to be serialized
   * @return a String representation of the serialized Location
   */
  public static String serializeLocation(Location l) {
    String s = "";
    s = s + "@w;" + l.getWorld().getName();
    s = s + ":@x;" + l.getX();
    s = s + ":@y;" + l.getY();
    s = s + ":@z;" + l.getZ();
    s = s + ":@p;" + l.getPitch();
    s = s + ":@ya;" + l.getYaw();
    return s;
  }

  /**
   * Deserialize a string representation of a Location object.
   *
   * @param  s  the string representation of the Location object
   * @return the deserialized Location object
   */
  public static Location deserializeLocation(String s) {
    Location l = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
    String[] att = s.split(":");
    int len$ = att.length;

    for (String attribute : att) {
      String[] split = attribute.split(";");
      if (split[0].equalsIgnoreCase("@w")) {
        l.setWorld(Bukkit.getWorld(split[1]));
      }

      if (split[0].equalsIgnoreCase("@x")) {
        l.setX(Double.parseDouble(split[1]));
      }

      if (split[0].equalsIgnoreCase("@y")) {
        l.setY(Double.parseDouble(split[1]));
      }

      if (split[0].equalsIgnoreCase("@z")) {
        l.setZ(Double.parseDouble(split[1]));
      }

      if (split[0].equalsIgnoreCase("@p")) {
        l.setPitch(Float.parseFloat(split[1]));
      }

      if (split[0].equalsIgnoreCase("@ya")) {
        l.setYaw(Float.parseFloat(split[1]));
      }
    }

    return l;
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
      if (type.getRawType() == Location.class) {
        return (TypeAdapter<T>) new LocationSerializer.LocationTypeAdapter();
      }
      return null;
    }
  }
}