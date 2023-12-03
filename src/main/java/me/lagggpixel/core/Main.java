package me.lagggpixel.core;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.listeners.PlayerStatsListeners;
import me.lagggpixel.core.listeners.onPlayerJoin;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.impl.CoreBazaarOffer;
import me.lagggpixel.core.modules.chat.ChatModule;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.home.HomeModule;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.inventory.InventoryModule;
import me.lagggpixel.core.modules.restart.RestartModule;
import me.lagggpixel.core.modules.rtp.RtpModule;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.modules.warp.WarpModule;
import me.lagggpixel.core.utils.LangUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import me.lagggpixel.core.utils.UserDataUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
  
  private final @NotNull HashMap<String, Module> modules = new HashMap<>();
  private final @NotNull Module bazaarModule = new BazaarModule();
  private final @NotNull Module chatModule = new ChatModule();
  private final @NotNull Module discordModule = new DiscordModule();
  private final @NotNull Module economyModule = new EconomyModule();
  private final @NotNull Module guildModule = new GuildModule();
  private final @NotNull Module homeModule = new HomeModule();
  private final @NotNull Module inventoryModule = new InventoryModule();
  private final @NotNull Module restartModule = new RestartModule();
  private final @NotNull Module rtpModule = new RtpModule();
  private final @NotNull Module skipnightModule = new SkipNightModule();
  private final @NotNull Module spawnModule = new SpawnModule();
  private final @NotNull Module staffModule = new StaffModule();
  private final @NotNull Module warpModule = new WarpModule();
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
   * @param  uuid  the UUID of the User to retrieve
   * @return       the User object associated with the specified UUID
   */
  public static User getUser(UUID uuid) {
    return userData.get(uuid);
  }

  public static @NotNull PluginManager getPluginManager() {
    return INSTANCE.getServer().getPluginManager();
  }

  @Override
  public void onEnable() {

    INSTANCE = this;

    LangUtils.loadLangConfig();

    userData = UserDataUtils.loadData();

    registerListeners();

    TeleportUtils.startTeleportTask();

    modules.put(bazaarModule.getId(), bazaarModule);
    modules.put(chatModule.getId(), chatModule);
    modules.put(discordModule.getId(), discordModule);
    modules.put(homeModule.getId(), homeModule);
    modules.put(inventoryModule.getId(), inventoryModule);
    modules.put(economyModule.getId(), economyModule);
    modules.put(guildModule.getId(), guildModule);
    modules.put(restartModule.getId(), restartModule);
    modules.put(rtpModule.getId(), rtpModule);
    modules.put(skipnightModule.getId(), skipnightModule);
    modules.put(spawnModule.getId(), spawnModule);
    modules.put(staffModule.getId(), staffModule);
    modules.put(warpModule.getId(), warpModule);

    EmbedBuilder startupLogEmbed = new EmbedBuilder();
    startupLogEmbed.setTitle("**Core Plugin Started**");

    modules.forEach((k, v) -> {
      if (v.isEnabled()) {
        log(Level.INFO, "Module " + v.getId() + " is enabled.");
        startupLogEmbed.addField(new MessageEmbed.Field(StringUtils.capitalize(v.getId()) + " module", "Enabled", true));
        v.onEnable();
        v.registerCommands();
        v.registerListeners();
      } else {
        startupLogEmbed.addField(new MessageEmbed.Field(StringUtils.capitalize(v.getId()) + " module", "Disabled", true));
        log(Level.INFO, "Module " + v.getId() + " is disabled.");
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
    UserDataUtils.saveData(userData);
    DiscordModule.discordManager.getJda().shutdown();
  }

  private void registerListeners() {
    new onPlayerJoin();
    new PlayerStatsListeners();

    TeleportUtils.PlayerTeleportCancelListener listener = new TeleportUtils.PlayerTeleportCancelListener();
    this.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
  }
}
