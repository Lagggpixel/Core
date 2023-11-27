package me.lagggpixel.core.modules.guilds.managers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.data.loadsave.GuildGsonSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GuildManager {
  
  private final List<Guild> guilds = new ArrayList<>();
  
  private static final String GUILD_DIRECTORY = Main.getInstance().getDataFolder() + "/data/modules/guild/guilds/";
  
  public void loadAllGuilds() {
    File guildDirectory = new File(GUILD_DIRECTORY);
    if (guildDirectory.exists() && guildDirectory.isDirectory()) {
      for (File guildFile : Objects.requireNonNull(guildDirectory.listFiles())) {
        if (guildFile.isFile() && guildFile.getName().endsWith(".json")) {
          Guild loadedGuild = GuildGsonSerializer.loadGuild(guildFile);
          if (loadedGuild != null) {
            guilds.add(loadedGuild);
          }
        }
      }
    }
  }
  
  public Guild createGuild(String guildName, UUID leaderUniqueId) {
    Guild guild = new Guild(guildName, leaderUniqueId);
    guilds.add(guild);
    saveGuild(guild);
    return guild;
  }
  
  public void saveAllGuilds() {
    for (Guild guild : guilds) {
      saveGuild(guild);
    }
  }
  
  private void saveGuild(Guild guild) {
    File leaderFile = new File(GUILD_DIRECTORY + guild.getLeader() + ".json");
    GuildGsonSerializer.saveGuild(guild, leaderFile);
  }
  
  public List<Guild> getGuilds() {
    return guilds;
  }
  
  public Guild getGuild(String name) {
    for (Guild guild : guilds) {
      if (guild.getName().equalsIgnoreCase(name)) {
        return guild;
      }
    }
    return null;
  }
  
  public String getGuildId(UUID playerUniqueId) {
    for (Guild guild : guilds) {
      if (guild.getMembers().contains(playerUniqueId)) {
        return guild.getName();
      }
    }
    return null;
  }
}
