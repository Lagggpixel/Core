/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.handlers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.data.loadsave.GuildLoadSave;
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GuildHandler {
  
  private final List<Guild> guilds = new ArrayList<>();
  
  private BukkitRunnable autoSave;
  
  private static final String GUILD_DIRECTORY = Main.getInstance().getDataFolder() + "/data/modules/guild/guilds/";
  private final int MAX_PLAYERS = 10;
  

  
  public Guild createGuild(String guildName, Player player) {
    Guild guild = new Guild(guildName, player.getUniqueId());
    GuildCreateEvent event = new GuildCreateEvent(player, guild);
    Main.getInstance().getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return null;
    }
    guilds.add(guild);
    return guild;
  }
  
  public Guild getGuildByName(String name) {
    for (Guild guild : guilds) {
      if (guild.getName().equalsIgnoreCase(name)) {
        return guild;
      }
    }
    return null;
  }
  
  public Guild getGuildFromPlayer(Player player) {
    UUID uuid = player.getUniqueId();
    for (Guild guild : guilds) {
      if (guild.isLeader(uuid) || guild.isOfficer(uuid) || guild.getMembers().contains(uuid)) {
        return guild;
      }
    }
    return null;
  }
  
  public Guild getGuildFromPlayerUUID(UUID playerUniqueId) {
    for (Guild guild : guilds) {
      if (guild.isLeader(playerUniqueId) || guild.isOfficer(playerUniqueId) || guild.getMembers().contains(playerUniqueId)) {
        return guild;
      }
    }
    return null;
  }
  
  public void disbandGuild(Player player, Guild guild) {
    GuildDisbandEvent event = new GuildDisbandEvent(player, guild);
    Main.getInstance().getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return;
    }
    if (guild != null) {
      guild.sendMessage(Lang.GUILD_DISBANDED_MEMBER.toComponentWithPrefix());
      guild.delete();
    }
  }
  
  public void startAutoSave() {
    if (autoSave == null || !autoSave.isCancelled()) {
      if (autoSave != null) {
        autoSave.cancel();
      }
      autoSave = new BukkitRunnable() {
        @Override
        public void run() {
          GuildLoadSave.save();
        }
      };
    }
    autoSave.runTaskTimerAsynchronously(Main.getInstance(), 20L * 60 * 10, 20L * 60 * 60);
  }
  
  public void stopAutoSave() {
    if (autoSave != null && !autoSave.isCancelled()) {
      autoSave.cancel();
    }
  }

  public int getMaxPlayers() {
    return MAX_PLAYERS;
  }

  public List<Material> getBlocks() {
    List<Material> blocks = new ArrayList<>();
    for (Material material : Material.values()) {
      if (material != Material.DIAMOND_BLOCK
          && !material.name().contains("SPAWNER")
          && !material.name().contains("STEP")
          && !material.name().contains("PLATE")
          && material.isSolid()
          && !material.name().contains("GLASS")
          && !material.name().contains("STAIRS")
          && !material.name().contains("FENCE")
          && !material.name().contains("SOIL")
          && !material.name().contains("BED")
          && !material.name().contains("DOOR")
          && !material.name().contains("PISTON")
          && !material.name().contains("DETECTOR")
          && !material.name().contains("FRAME")
          && !material.name().contains("COMMAND")
          && !material.name().contains("SIGN")
          && !material.name().contains("CAKE")
          && !material.name().contains("CACTUS")
          && !material.name().contains("HOPPER")
          && !material.name().contains("CHEST")
          && !material.name().contains("LEAVES")
          && !material.name().contains("EGG")) {
        blocks.add(material);
      }
    }
    return blocks;
  }
}
