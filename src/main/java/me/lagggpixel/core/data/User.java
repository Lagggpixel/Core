package me.lagggpixel.core.data;

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
  private final @NotNull UUID playerUUID;
  private @NotNull String playerName;
  private boolean afk = false;
  // Player stats
  private final @NotNull HashMap<EntityType, Long> entityKills;
  private final @NotNull HashMap<Material, Long> blocksBroken;
  private final @NotNull HashMap<Material, Long> blocksPlaced;
  private final @NotNull HashMap<Material, Long> itemsCrafted;
  // Discord
  private @Nullable Long discordId;
  // Economy
  private double playerBalance;
  // Homes
  private Map<String, Home> homes;
  // Staff configurations
  private InstantPlayerData instantPlayerData;
  private boolean staffMode;
  private boolean isVanished;
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
    this.entityKills = (HashMap<EntityType, Long>) map.get("entityKills");
    this.blocksBroken = (HashMap<Material, Long>) map.get("blocksBroken");
    this.blocksPlaced = (HashMap<Material, Long>) map.get("blocksPlaced");
    this.itemsCrafted = (HashMap<Material, Long>) map.get("itemsCrafted");
    
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
      put("entityKills", entityKills);
      put("blocksBroken", blocksBroken);
      put("blocksPlaced", blocksPlaced);
      put("itemsCrafted", itemsCrafted);
      
      // Discord
      put("discordId", discordId);

      
      // Economy
      put("balance", playerBalance);
      
      // Homes
      put("homes", homes);
      
      // Staff
      put("instantPlayerData", instantPlayerData.serialize());
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
