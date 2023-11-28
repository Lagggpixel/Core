package me.lagggpixel.core.modules.guilds.handlers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.data.loadsave.GuildGsonSerializer;
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class GuildHandler {
  
  private final List<Guild> guilds = new ArrayList<>();
  
  private BukkitRunnable autoSave;
  
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
  
  public Guild createGuild(String guildName, Player player) {
    Guild guild = new Guild(guildName, player.getUniqueId());
    GuildCreateEvent event = new GuildCreateEvent(player, guild);
    Main.getInstance().getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return null;
    }
    guilds.add(guild);
    saveGuild(guild);
    return guild;
  }
  
  public void saveAllGuilds() {
    File file = new File(GUILD_DIRECTORY);
    if (file.isDirectory() && file.listFiles() != null) {
      for (File guildFile : Objects.requireNonNull(file.listFiles())) {
        guildFile.delete();
      }
    }
    for (Guild guild : guilds) {
      saveGuild(guild);
    }
  }
  
  private void saveGuild(Guild guild) {
    File leaderFile = new File(GUILD_DIRECTORY + guild.getLeader() + ".json");
    GuildGsonSerializer.saveGuild(guild, leaderFile);
  }
  
  public Guild getGuild(String name) {
    for (Guild guild : guilds) {
      if (guild.getName().equalsIgnoreCase(name)) {
        return guild;
      }
    }
    return null;
  }
  
  public String getGuildName(UUID playerUniqueId) {
    for (Guild guild : guilds) {
      if (guild.getMembers().contains(playerUniqueId)) {
        return guild.getName();
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
      guilds.remove(guild);
      
      for (UUID uuid: guild.getMembers()) {
        if (uuid.equals(guild.getLeader())) {
          continue;
        }
        Player p1 = Main.getInstance().getServer().getPlayer(uuid);
        if (p1 != null) {
          p1.sendMessage(Lang.GUILD_DISBANDED_MEMBER.toComponentWithPrefix());
        }
      }
    }
  }
  
  public void startAutoSave() {
    if (!autoSave.isCancelled() || autoSave == null) {
      if (autoSave != null) {
        autoSave.cancel();
      }
      autoSave = new BukkitRunnable() {
        @Override
        public void run() {
          saveAllGuilds();
        }
      };
    }
    autoSave.runTaskTimerAsynchronously(Main.getInstance(), 20L * 60 * 10, 20L * 60 * 60);
  }
  
  public void stopAutoSave() {
    autoSave.cancel();
  }
  
}
