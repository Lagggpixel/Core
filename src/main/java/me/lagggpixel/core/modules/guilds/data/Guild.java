/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.loadsave.GuildLoadSave;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.serializers.LocationSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@SuppressWarnings("unused")
@Getter
@Setter
@Data
@AllArgsConstructor
public class Guild {

  private File file;
  private YamlConfiguration configuration;
  @Getter
  private YamlConfiguration config = getConfiguration();

  @Setter
  private String name;
  @Setter
  private Location home;
  @Getter
  private GuildModule main = GuildModule.getInstance();
  @Setter
  @Getter
  private UUID leader;
  @Setter
  @Getter
  private ArrayList<UUID> officers;
  @Setter
  @Getter
  private ArrayList<UUID> members;
  @Setter
  @Getter
  private ArrayList<UUID> invitedPlayers;
  @Setter
  @Getter
  private double balance;
  @Setter
  @Getter
  private HashSet<Guild> allies;
  @Setter
  @Getter
  private HashSet<Guild> requestedAllies;
  @Setter
  @Getter
  private List<UUID> allyChat;
  @Setter
  @Getter
  private List<UUID> factionChat;
  @Setter
  @Getter
  private boolean deleted = false;
  
  public Guild(String name, UUID leader) {
    this.name = name;
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
    for (Guild ally : getAllies()) {
      ally.getAllies().remove(this);
    }
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

  public void save() throws IOException {
    if (this.deleted) {
      return;
    }

    this.file = new File(GuildLoadSave.getFolder(), getName().toLowerCase() + ".yml");

    if (!this.file.exists()) {
      if (!this.file.createNewFile()) {
        throw new IOException("Could not create guild file: " + this.file.getName());
      }
    }

    ArrayList<String> mems = new ArrayList<>();
    ArrayList<String> ofs = new ArrayList<>();
    ArrayList<String> invs = new ArrayList<>();
    ArrayList<String> als = new ArrayList<>();

    for (UUID memsid : getMembers()) {
      mems.add(memsid.toString());
    }
    for (UUID ofsid : getOfficers()) {
      ofs.add(ofsid.toString());
    }
    for (Guild guild : getAllies()) {
      als.add(guild.getName());
    }
    for (UUID uuid : this.invitedPlayers) {
      invs.add(uuid.toString());
    }

    this.config = YamlConfiguration.loadConfiguration(this.file);
    for (String string : this.config.getKeys(false)) {
      this.config.set(string, null);
    }
    this.config.save(this.file);
    this.config.set("name", getName());
    this.config.set("leader", this.leader.toString());
    this.config.set("officers", ofs);
    this.config.set("members", mems);
    this.config.set("allies", als);
    this.config.set("invited_players", invs);
    this.config.set("balance", this.balance);
    if (getHome() != null) {
      this.config.set("home", LocationSerializer.serializeLocation(getHome()));
    }
    this.config.save(this.file);
  }
}
