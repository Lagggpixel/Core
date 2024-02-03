/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.skills.data.Skills;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@SuppressWarnings("unused")
@Data
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
  // Player data
  @SerializedName("PlayerUUID")
  @Expose
  private final @NotNull UUID playerUUID;
  // Player stats
  @SerializedName("EntityKills")
  @Expose
  private final @NotNull HashMap<EntityType, Long> entityKills = new HashMap<>();
  @SerializedName("BlocksBroken")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksBroken = new HashMap<>();
  @SerializedName("BlocksPlaced")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksPlaced = new HashMap<>();
  @SerializedName("PlayerName")
  @Expose
  private @NotNull String playerName = "null";
  @SerializedName("QueuedMessages")
  @Expose
  private @NotNull List<Component> getQueuedMessage = new ArrayList<>();
  private transient boolean afk = false;
  // Discord
  @SerializedName("DiscordID")
  @Expose
  private @Nullable Long discordId = null;
  // Economy
  @SerializedName("PlayerBalance")
  @Expose
  private double playerBalance;
  // Homes
  @SerializedName("Homes")
  @Expose
  private @NotNull Map<String, Home> homes = new HashMap<>();
  // Merchant
  @Getter
  private @NotNull List<ItemStack> merchantSold = new ArrayList<>();
  private @Nullable String currentMerchant = null;
  // Skills
  @SerializedName("Skills")
  @Expose
  private final @NotNull Skills skills;
  // Staff configurations
  @SerializedName("InstantPlayerData")
  @Expose
  private @Nullable InstantPlayerData instantPlayerData = null;
  @SerializedName("StaffMode")
  @Expose
  private boolean staffMode = false;
  @SerializedName("IsVanished")
  @Expose
  private boolean isVanished = false;
  @SerializedName("StaffChatToggled")
  @Expose
  private boolean staffChatToggled = false;

  /**
   * Constructs a new user.
   *
   * @param player The player object.
   */
  public User(@NotNull Player player) {
    this.playerUUID = player.getUniqueId();
    this.playerName = player.getName();

    this.skills = new Skills(player.getUniqueId());
  }

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

  /**
   * Sends a message using the given string.
   * This will queue the message if the user is not online
   *
   * @param string the string used for sending the message
   */
  public boolean sendMessage(String string) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
    if (player.isOnline() && player.getPlayer() != null) {
      player.getPlayer().sendMessage(string);
      return true;
    }
    queueMessage(ChatUtils.stringToComponentCC(string));
    return false;
  }

  /**
   * Sends a message using the given component.
   * This will queue the message if the user is not online
   *
   * @param component the component used for sending the message
   */
  public boolean sendMessage(@NotNull Component component) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
    if (player.isOnline() && player.getPlayer() != null) {
      player.getPlayer().sendMessage(component);
      return true;
    }
    queueMessage(component);
    return false;
  }

  /**
   * A method to queue a message with the given component.
   *
   * @param  component   the component to be queued
   */
  private void queueMessage(Component component) {
    this.getQueuedMessage.add(component);
  }

  /**
   * Check if the player is online.
   *
   * @return true if the player is online, false otherwise
   */
  public boolean isOnline() {
    return Bukkit.getPlayer(playerUUID) != null;
  }

  /**
   * Retrieves the current Merchant the user has selected.
   *
   * @return the current Merchant object, or null if it does not exist
   */
  public Merchant getCurrentMerchant() {
    if (currentMerchant == null) {
      return null;
    }
    if (!MerchantModule.getInstance().getMerchantHandler().hasMerchant(currentMerchant)) {
      currentMerchant = null;
      return null;
    }
    return MerchantModule.getInstance().getMerchantHandler().getMerchant(currentMerchant);
  }
}
