package me.lagggpixel.core;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.listeners.PlayerStatsListeners;
import me.lagggpixel.core.listeners.onPlayerJoin;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.impl.CoreBazaarOffer;
import me.lagggpixel.core.modules.chat.ChatModule;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.home.HomeModule;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.modules.warp.WarpModule;
import me.lagggpixel.core.serializers.UserDataSerializer;
import me.lagggpixel.core.utils.LangUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
  
  private static Main INSTANCE;
  private static Map<UUID, User> userData;
  
  static {
    ConfigurationSerialization.registerClass(InstantPlayerData.class);
    ConfigurationSerialization.registerClass(Home.class);
    ConfigurationSerialization.registerClass(CoreBazaarOffer.class);
  }
  
  private final @NotNull HashMap<String, IModule> modules = new HashMap<>();
  private final @NotNull IModule bazaarModule = new BazaarModule();
  private final @NotNull IModule chatModule = new ChatModule();
  private final @NotNull IModule discordModule = new DiscordModule();
  private final @NotNull IModule economyModule = new EconomyModule();
  private final @NotNull IModule guildModule = new GuildModule();
  private final @NotNull IModule homeModule = new HomeModule();
  private final @NotNull IModule skillModule = new SkillsModule();
  private final @NotNull IModule skipnightModule = new SkipNightModule();
  private final @NotNull IModule spawnModule = new SpawnModule();
  private final @NotNull IModule staffModule = new StaffModule();
  private final @NotNull IModule warpModule = new WarpModule();
  @Setter
  @Getter
  private Logger log4jLogger;
  
  public static @Nonnull Main getInstance() {
    return INSTANCE;
  }
  
  public static void log(Level level, String message) {
    Main.getInstance().getLogger().log(level, "[Infinite Minecrafters Core]: " + message);
  }
  
  public static @Nonnull Map<UUID, User> getUserData() {
    return userData;
  }
  
  /**
   * Retrieves the User object associated with the specified UUID.
   *
   * @param uuid the UUID of the User to retrieve
   * @return the User object associated with the specified UUID
   */
  public static @NotNull User getUser(@NotNull UUID uuid) {
    if (!userData.containsKey(uuid)) {
      userData.put(uuid, new User(uuid));
    }
    return userData.get(uuid);
  }
  
  /**
   * Retrieves the User object associated with the specified Player Object.
   *
   * @param player the Player object of the User to retrieve
   * @return the User object associated with the specified UUID
   */
  public static User getUser(Player player) {
    return userData.get(player.getUniqueId());
  }
  
  public static @NotNull PluginManager getPluginManager() {
    return INSTANCE.getServer().getPluginManager();
  }
  
  @Override
  public void onEnable() {
    
    INSTANCE = this;
    
    LangUtils.loadLangConfig();
    
    userData = UserDataSerializer.loadData();
    
    registerListeners();
    
    TeleportUtils.startTeleportTask();
    
    modules.put(bazaarModule.getId(), bazaarModule);
    modules.put(chatModule.getId(), chatModule);
    modules.put(discordModule.getId(), discordModule);
    modules.put(homeModule.getId(), homeModule);
    modules.put(skillModule.getId(), skillModule);
    modules.put(economyModule.getId(), economyModule);
    modules.put(guildModule.getId(), guildModule);
    modules.put(skipnightModule.getId(), skipnightModule);
    modules.put(spawnModule.getId(), spawnModule);
    modules.put(staffModule.getId(), staffModule);
    modules.put(warpModule.getId(), warpModule);
    
    EmbedBuilder startupLogEmbed = new EmbedBuilder();
    startupLogEmbed.setTitle("**Core Plugin Started**");
    
    modules.forEach((k, v) -> {
      if (v.isEnabled()) {
        log(Level.INFO, "IModule " + v.getId() + " is enabled.");
        startupLogEmbed.addField(new MessageEmbed.Field(StringUtils.capitalize(v.getId()) + " module", "Enabled", true));
        v.onEnable();
        v.registerCommands();
        v.registerListeners();
      } else {
        startupLogEmbed.addField(new MessageEmbed.Field(StringUtils.capitalize(v.getId()) + " module", "Disabled", true));
        log(Level.INFO, "IModule " + v.getId() + " is disabled.");
      }
    });
    
    DiscordModule.discordManager.sendEmbed(DiscordModule.discordManager.LOGGING_CHANNEL, startupLogEmbed.build());
  }
  
  @Override
  public void onDisable() {
    modules.forEach((k, v) -> {
      if (v.isEnabled()) {
        v.onDisable();
      }
    });
    DiscordModule.discordManager.sendEmbed(DiscordModule.discordManager.LOGGING_CHANNEL, new EmbedBuilder().setTitle("**Core Plugin Disabled**").build());
    UserDataSerializer.saveData(userData);
    DiscordModule.discordManager.getJda().shutdown();
  }
  
  private void registerListeners() {
    new onPlayerJoin();
    new PlayerStatsListeners();
    
    TeleportUtils.PlayerTeleportCancelListener listener = new TeleportUtils.PlayerTeleportCancelListener();
    this.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
  }
}
