package me.lagggpixel.core.modules.guilds.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@Getter
@Setter
@Data
@AllArgsConstructor
public class Guild {
  private String name;
  private YamlConfiguration configuration;
  private HashSet<Claim> claims;
  private Location home;
  @Getter
  private GuildModule main = GuildModule.getInstance();
  @Getter
  private UUID leader;
  @Getter
  private ArrayList<UUID> officers;
  @Getter
  private ArrayList<UUID> members;
  @Getter
  private ArrayList<UUID> invitedPlayers;
  @Getter
  private int balance;
  @Getter
  private HashSet<Guild> allies;
  @Getter
  private HashSet<Guild> requestedAllies;
  @Getter
  private List<UUID> allyChat;
  @Getter
  private List<UUID> factionChat;
  @Getter
  private boolean deleted = false;
  
  public Guild(String name, UUID leader) {
    this.name = name;
    this.claims = new HashSet<>();
    this.leader = leader;
    this.allyChat = new ArrayList<>();
    this.factionChat = new ArrayList<>();
    this.allies = new HashSet<>();
    this.invitedPlayers = new ArrayList<>();
    this.requestedAllies = new HashSet<>();
    this.officers = new ArrayList<>();
    this.members = new ArrayList<>();
    this.balance = 0;
  }
  
  public void setLeader(UUID leader) {
    this.leader = leader;
  }
  
  public void setOfficers(ArrayList<UUID> officers) {
    this.officers = officers;
  }
  
  public void setMembers(ArrayList<UUID> members) {
    this.members = members;
  }
  
  public void setBalance(int balance) {
    this.balance = balance;
  }
  
  public void setAllies(HashSet<Guild> allies) {
    this.allies = allies;
  }
  
  public void setRequestedAllies(HashSet<Guild> requestedAllies) {
    this.requestedAllies = requestedAllies;
  }
  
  public void setAllyChat(List<UUID> allyChat) {
    this.allyChat = allyChat;
  }
  
  public void setFactionChat(List<UUID> factionChat) {
    this.factionChat = factionChat;
  }
  
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
  
  public void setInvitedPlayers(ArrayList<UUID> invitedPlayers) {
    this.invitedPlayers = invitedPlayers;
  }
  
  public ArrayList<Player> getOnlinePlayers() {
    ArrayList<Player> onlinePlayers = new ArrayList<>();
    
    for (UUID id : getIDs()) {
      Player player = Bukkit.getPlayer(id);
      if (player != null) {
        onlinePlayers.add(player);
      }
    }
    
    return onlinePlayers;
  }
  
  public ArrayList<OfflinePlayer> getPlayers() {
    ArrayList<OfflinePlayer> offlinePlayers = new ArrayList<>();
    for (UUID ids : getIDs()) {
      offlinePlayers.add(Bukkit.getOfflinePlayer(ids));
    }
    
    return offlinePlayers;
  }
  
  public boolean isAlly(Player p) {
    for (Guild faction : getAllies()) {
      if (faction.getPlayers().contains(p)) {
        return true;
      }
    }
    return false;
  }
  
  public OfflinePlayer getPlayer(String name) {
    for (OfflinePlayer offlinePlayer : getPlayers()) {
      if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(name)) {
        return offlinePlayer;
      }
    }
    return null;
  }
  
  public ArrayList<UUID> getIDs() {
    ArrayList<UUID> ids = new ArrayList<>();
    ids.add(this.leader);
    ids.addAll(getOfficers());
    ids.addAll(getMembers());
    
    return ids;
  }
  
  public boolean isLeader(UUID id) {
    return this.leader.toString().equals(id.toString());
  }
  
  public void delete() {
    main.getGuildHandler().getGuilds().remove(this);
    this.deleted = true;
    for (Claim claim : getClaims()) {
      main.getClaimManager().getClaims().remove(claim);
    }
    for (Guild ally : getAllies()) {
      ally.getAllies().remove(this);
    }
    getClaims().clear();
  }
  
  public void sendMessage(String message) {
    for (Player online : getOnlinePlayers()) {
      online.sendMessage(ChatUtils.stringToComponentCC(message));
    }
  }
  
  public void sendMessage(Component message) {
    for (Player online : getOnlinePlayers()) {
      online.sendMessage(message);
    }
  }
  
  public boolean isOfficer(Player player) {
    return getOfficers().contains(player.getUniqueId());
  }
  
  public boolean isOfficer(UUID player) {
    return getOfficers().contains(player);
  }
  
  /**
   * Sets the name of the object.
   *
   * @param name the new name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Sets the configuration for the object.
   *
   * @param configuration the YAML configuration to set
   */
  public void setConfiguration(YamlConfiguration configuration) {
    this.configuration = configuration;
  }
  
  /**
   * Sets the claims for the object.
   *
   * @param claims the set of claims to be set
   */
  public void setClaims(HashSet<Claim> claims) {
    this.claims = claims;
  }
  
  /**
   * Sets the home location.
   *
   * @param  home the new home location
   */
  public void setHome(Location home) {
    this.home = home;
  }
  
  /**
   * Checks if the given location is near the border of a claim.
   *
   * @param  l  the location to check
   * @return    true if the location is near a claim border, false otherwise
   */
  public boolean isNearBorder(Location l) {
    for (Claim claim : getClaims()) {
      if (claim.getWorld() == l.getWorld()) {
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(0.0D, 0.0D, 1.0D), false)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(1.0D, 0.0D, 0.0D), false)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(0.0D, 0.0D, -1.0D), true)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-1.0D, 0.0D, 0.0D), true)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-1.0D, 0.0D, 1.0D), false)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(-1.0D, 0.0D, -1.0D), false)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(1.0D, 0.0D, 1.0D), false)) {
          return true;
        }
        if (claim.isInside((new Location(l.getWorld(), l.getX(), l.getY(), l.getZ())).add(1.0D, 0.0D, -1.0D), false)) {
          return true;
        }
      }
    }
    return false;
  }
}
