package me.lagggpixel.core.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class User implements ConfigurationSerializable {
  // Player data
  @SerializedName("PlayerUUID")
  @Expose
  private final @NotNull UUID playerUUID;
  @SerializedName("PlayerName")
  @Expose
  private @NotNull String playerName;
  private transient boolean afk = false;
  // Player stats
  @SerializedName("EntityKills")
  @Expose
  private final @NotNull HashMap<EntityType, Long> entityKills;
  @SerializedName("BlocksBroken")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksBroken;
  @SerializedName("BlocksPlaced")
  @Expose
  private final @NotNull HashMap<Material, Long> blocksPlaced;
  @SerializedName("ItemsCrafted")
  @Expose
  private final @NotNull HashMap<Material, Long> itemsCrafted;
  // Discord
  @SerializedName("DiscordID")
  @Expose
  private @Nullable Long discordId;
  // Economy
  @SerializedName("PlayerBalance")
  @Expose
  private double playerBalance;
  // Homes
  @SerializedName("Homes")
  @Expose
  private Map<String, Home> homes;
  // Staff configurations
  @SerializedName("InstantPlayerData")
  @Expose
  private InstantPlayerData instantPlayerData;
  @SerializedName("StaffMode")
  @Expose
  private boolean staffMode;
  @SerializedName("IsVanished")
  @Expose
  private boolean isVanished;
  @SerializedName("StaffChatToggled")
  @Expose
  private boolean staffChatToggled;
  
  /**
   * Constructs a new user.
   *
   * @param player The player object.
   */
  public User(@NotNull Player player) {
    // Player data
    this.playerUUID = player.getUniqueId();
    this.playerName = player.getName();
    
    // Player stats
    this.entityKills = new HashMap<>();
    this.blocksBroken = new HashMap<>();
    this.blocksPlaced = new HashMap<>();
    this.itemsCrafted = new HashMap<>();
    
    // Discord
    
    // Economy
    this.playerBalance = 0.00;
    
    // Homes
    this.homes = new HashMap<>();
    
    // Staff
    this.instantPlayerData = null;
    this.staffMode = false;
    this.isVanished = false;
    this.staffChatToggled = false;
  }
  
  public User(@NotNull Map<String, Object> map) {
    // Player data configuration
    this.playerName = String.valueOf(map.get("name"));
    this.playerUUID = UUID.fromString(String.valueOf(map.get("uuid")));
    
    // Player stats configuration
    map.get("entityKills");
    this.entityKills = (HashMap<EntityType, Long>) map.getOrDefault("entityKills", new HashMap<>());
    this.blocksBroken = (HashMap<Material, Long>) map.getOrDefault("blocksBroken", new HashMap<>());
    this.blocksPlaced = (HashMap<Material, Long>) map.getOrDefault("blocksPlaced", new HashMap<>());
    this.itemsCrafted = (HashMap<Material, Long>) map.getOrDefault("itemsCrafted", new HashMap<>());
    
    // Discord configuration
    this.discordId = Long.getLong(String.valueOf(map.get("discordId")));
    
    // Economy configuration
    this.playerBalance = Double.parseDouble(String.valueOf(map.getOrDefault("balance", "0.0")));
    
    // Homes configuration
    // noinspection unchecked
    this.homes = (Map<String, Home>) map.get("homes");
    
    // Staff configuration
    if (map.containsKey("instantPlayerData") && map.get("instantPlayerData") != null) {
      this.instantPlayerData = InstantPlayerData.deserialize((Map<String, Object>) map.get("instantPlayerData"));
    }
    this.staffMode = (boolean) map.getOrDefault("staffMode", false);
    this.isVanished = (boolean) map.getOrDefault("vanished", false);
    this.staffChatToggled = (boolean) map.getOrDefault("staffChatToggled", false);
  }
  
  @Override
  public @NotNull Map<String, Object> serialize() {
    return new HashMap<>() {{
      // Player data
      put("name", playerName);
      put("uuid", playerUUID.toString());
      
      // Player stats
      entityKills.forEach((key, value) -> put("entityKills." + key.toString(), value));
      blocksBroken.forEach((key, value) -> put("blocksBroken." + key.toString(), value));
      blocksPlaced.forEach((key, value) -> put("blocksPlaced." + key.toString(), value));
      itemsCrafted.forEach((key, value) -> put("itemsCrafted." + key.toString(), value));
      
      // Discord
      put("discordId", discordId);
      
      // Economy
      put("balance", playerBalance);
      
      // Homes
      put("homes", homes);
      
      // Staff
      if (instantPlayerData != null) {
        put("instantPlayerData", instantPlayerData.serialize());
      }
      else {
        put("instantPlayerData", null);
      }
      put("staffMode", staffMode);
      put("vanished", isVanished);
      put("staffChatToggled", staffChatToggled);
    }};
  }
  
  public long getEntityKills(EntityType entityType) {
    return entityKills.getOrDefault(entityType, 0L);
  }
  public long getBlockBroken(Material material) {
    return blocksBroken.getOrDefault(material, 0L);
  }
  public long getBlockPlaced(Material material) {
    return blocksPlaced.getOrDefault(material, 0L);
  }
  public long getItemCrafted(Material material) {
    return itemsCrafted.getOrDefault(material, 0L);
  }
  
  public long getTotalEntityKills() {
    long total = 0;
    for (Map.Entry<EntityType, Long> entry : entityKills.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }
  public long getTotalBlocksBroken() {
    long total = 0;
    for (Map.Entry<Material, Long> entry : blocksBroken.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }
  public long getTotalBlocksPlaced() {
    long total = 0;
    for (Map.Entry<Material, Long> entry : blocksPlaced.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }
  public long getTotalItemsCrafted() {
    long total = 0;
    for (Map.Entry<Material, Long> entry : itemsCrafted.entrySet()) {
      total += entry.getValue();
    }
    return total;
  }
}
