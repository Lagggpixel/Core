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
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lagggpixel
 * @since February 03, 2024
 */
@SuppressWarnings("unused")
@Data
@Getter
public class UserStats {
  @SerializedName("EntityKills")
  @Expose
  private final @NotNull HashMap<EntityType, Long> entityKills = new HashMap<>();
  @SerializedName("BlocksBroken")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksBroken = new HashMap<>();
  @SerializedName("BlocksPlaced")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksPlaced = new HashMap<>();

  /**
   * Gets the number of kills for a specific entity type.
   *
   * @param  entityType  the type of the entity
   * @return the number of kills for the specified entity type, or 0 if no kills are recorded
   */
  public long getEntityKills(EntityType entityType) {
    return entityKills.getOrDefault(entityType, 0L);
  }

  /**
   * Retrieves the number of blocks of the specified material that have been broken.
   *
   * @param  material  the material of the blocks
   * @return the number of blocks broken of the specified material
   */
  public long getBlockBroken(Material material) {
    return blocksBroken.getOrDefault(material, 0L);
  }

  /**
   * Retrieves the number of blocks of a specific material that have been placed.
   *
   * @param  material   the material to retrieve the number of placed blocks for
   * @return the number of placed blocks of the specified material
   */
  public long getBlockPlaced(Material material) {
    return blocksPlaced.getOrDefault(material, 0L);
  }

  /**
   * Returns the total number of kills for each entity type.
   *
   * @return the total number of kills for all entity types
   */
  public long getTotalEntityKills() {
    long total = 0;
    for (Map.Entry<EntityType, Long> entry : entityKills.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }

  /**
   * Calculates the total number of blocks broken.
   *
   * @return the total number of blocks broken
   */
  public long getTotalBlocksBroken() {
    long total = 0;
    for (Map.Entry<Material, Long> entry : blocksBroken.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }

  /**
   * Get the total number of blocks placed.
   *
   * @return the total number of blocks placed
   */
  public long getTotalBlocksPlaced() {
    long total = 0;
    for (Map.Entry<Material, Long> entry : blocksPlaced.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }
}
