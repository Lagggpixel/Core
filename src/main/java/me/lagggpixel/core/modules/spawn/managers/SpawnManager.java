/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.spawn.managers;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SpawnManager {
  
  private final File dataFolder = new File(Main.getInstance().getDataFolder(), "data/modules/spawn");
  private final String dataFileName = "spawn_data.yml";
  private final File file = new File(dataFolder, dataFileName);
  
  @Getter
  @Setter
  private Location spawnLocation;
  
  public void loadSpawnLocation() {
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    try {
      spawnLocation = config.getSerializable("location", Location.class);
    } catch (Exception exception) {
      spawnLocation = null;
      EmbedBuilder embedBuilder = new EmbedBuilder().setTimestamp(java.time.Instant.now()).setColor(Color.RED);
      embedBuilder.setAuthor("Spawn location was not found");
      embedBuilder.addField("Location in memory", String.valueOf(config.getString("location")), false);
      embedBuilder.setFooter("SpawnManager.loadSpawnLocation");
      DiscordHandler.getInstance().sendEmbed(DiscordModule.discordHandler.LOGGING_CHANNEL, embedBuilder.build());
    }
  }
  
  public void saveSpawnLocation() {
    if (spawnLocation == null) {
      return;
    }
    
    try {
      if (!dataFolder.exists()) {
        dataFolder.mkdir();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      
      config.set("location", spawnLocation);
      config.save(file);
      
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
  }
  
}
