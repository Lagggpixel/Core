/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.data.loadsave;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.serializers.LocationSerializer;
import me.lagggpixel.core.utils.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class GuildLoadSave {
  @Getter
  private static final String folder = Main.getInstance().getDataFolder() + "/data/modules/guild/";

  public static void load() throws NullPointerException {
    File folder = new File(GuildLoadSave.folder);
    if (!folder.exists()) {
      if (!folder.mkdir()) {
        Main.log(Level.WARNING, "Guild data folder didn't create successfully: " + folder.getName());
      }
      return;
    }

    if (!folder.isDirectory()) {
      Main.log(Level.WARNING, "Guild data folder is not a directory: " + folder.getName());
      return;
    }

    if (!folder.canRead()) {
      Main.log(Level.WARNING, "Guild data folder is not readable: " + folder.getName());
      return;
    }

    if (folder.list() == null) {
      return;
    }

    if (folder.listFiles() == null) {
      return;
    }

    Main.log(Level.INFO, "Preparing to load " + (Objects.requireNonNull(folder.list())).length + " guilds.");
    for (String filePath : Objects.requireNonNull(folder.list())) {
      File file = new File(folder.getPath() + File.separator + filePath);
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      String name = config.getString("name");
      if (name != null) {
        if (!file.getName().contains(name)) {
          if (!file.renameTo(new File(folder, name.toLowerCase() + ".yml"))) {
            Main.log(Level.WARNING, "Failed to rename guild file: " + file.getName());
          }
          try {
            config.save(file);
          } catch (IOException e) {
            Main.log(Level.WARNING, "Failed to save guild file: " + file.getName());
            ExceptionUtils.handleException(e);
          }
        }
      }

      UUID leader = UUID.fromString(Objects.requireNonNull(config.getString("leader")));
      int balance = config.getInt("balance");
      ArrayList<UUID> officers = new ArrayList<>();
      ArrayList<UUID> members = new ArrayList<>();
      ArrayList<UUID> invitedPlayers = new ArrayList<>();

      for (String ofs : config.getStringList("officers")) {
        officers.add(UUID.fromString(ofs));
      }

      for (String mems : config.getStringList("members")) {
        members.add(UUID.fromString(mems));
      }

      for (String invs : config.getStringList("invited_players")) {
        invitedPlayers.add(UUID.fromString(invs));
      }

      final List<String> allies = config.getStringList("allies");


      Location home = null;
      if (config.contains("home")) {
        home = LocationSerializer.deserializeLocation(Objects.requireNonNull(config.getString("home")));
      }
      final Guild guild = new Guild(name, leader);
      guild.setBalance(balance);
      guild.setHome(home);
      guild.setInvitedPlayers(invitedPlayers);
      guild.setMembers(members);
      guild.setOfficers(officers);
      (new BukkitRunnable() {
        public void run() {
          for (String name : allies) {
            if (GuildModule.getInstance().getGuildHandler().getGuildByName(name) != null) {
              guild.getAllies().add(GuildModule.getInstance().getGuildHandler().getGuildByName(name));
            }
          }
        }
      }).runTaskLater(Main.getInstance(), 20L);
      if (config.contains("claims")) {
        for (String c : Objects.requireNonNull(config.getConfigurationSection("claims")).getKeys(false)) {
          int x1 = config.getInt("claims." + c + ".x1");
          int x2 = config.getInt("claims." + c + ".x2");
          int z1 = config.getInt("claims." + c + ".z1");
          int z2 = config.getInt("claims." + c + ".z2");
          int value = config.getInt("claims." + c + ".value");
          boolean claimExplosions = config.getBoolean("claims." + c + ".claim_explosions", false);
          World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("claims." + c + ".world")));
          Claim claim = new Claim(c, guild, x1, x2, z1, z2, world, value);
          claim.setClaimExplosions(claimExplosions);
          if (!claim.isGlitched()) {
            GuildModule.getInstance().getClaimManager().getClaims().add(claim);
            guild.getClaims().add(claim);
          }
        }
      }
      if (GuildModule.getInstance().getGuildHandler().getGuildByName(name) == null && GuildModule.getInstance().getGuildHandler().getGuildFromPlayerUUID(guild.getLeader()) == null) {
        GuildModule.getInstance().getGuildHandler().getGuilds().add(guild);
      }
    }
  }

  public static void save() {
    for (Guild guild : GuildModule.getInstance().getGuildHandler().getGuilds()) {
      try {
        guild.save();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
  }
}

