package me.lagggpixel.core.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.skills.data.Skills;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import org.bukkit.Material;
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
public class User {
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
  // Skills
  @SerializedName("Skills")
  @Expose
  private Skills skills;
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
    
    // Discord
    
    // Economy
    this.playerBalance = 0.00;
    
    // Homes
    this.homes = new HashMap<>();

    // Skills
    this.skills = new Skills();
    
    // Staff
    this.instantPlayerData = null;
    this.staffMode = false;
    this.isVanished = false;
    this.staffChatToggled = false;
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
}
