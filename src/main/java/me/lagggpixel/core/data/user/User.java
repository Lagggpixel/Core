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
import lombok.*;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.skills.data.Skills;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
  @SerializedName("PlayerName")
  @Expose
  private @NotNull String playerName = "null";
  @SerializedName("QueuedMessages")
  @Expose
  private @NotNull List<Component> getQueuedMessage = new ArrayList<>();
  @SerializedName("WorldData")
  @Expose
  private @NotNull Map<String, UserWorldData> worldData = new HashMap<>();
  private transient boolean afk = false;
  // Other
  @SerializedName("UserStats")
  @Expose
  private final UserStats userStats = new UserStats();
  @SerializedName("UserPreference")
  @Expose
  private final UserPreference userPreference = new UserPreference();
  // Discord
  @SerializedName("DiscordID")
  @Expose
  private @Nullable Long discordId = null;
  // Economy
  @SerializedName("PlayerBalance")
  @Expose
  private double playerBalance = 0.0;
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
