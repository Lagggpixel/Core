package me.lagggpixel.core;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.listeners.onPlayerJoin;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.chat.ChatModule;
import me.lagggpixel.core.modules.chatgames.ChatgamesModule;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.home.HomeModule;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.inventory.InventoryModule;
import me.lagggpixel.core.modules.restart.RestartModule;
import me.lagggpixel.core.modules.rtp.RtpModule;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.warp.WarpModule;
import me.lagggpixel.core.utils.TeleportUtils;
import me.lagggpixel.core.utils.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
  
  static {
    ConfigurationSerialization.registerClass(User.class);
    ConfigurationSerialization.registerClass(Home.class);
  }
  
  private static Main INSTANCE;
  private static Map<UUID, User> userData;
  
  private final @NotNull HashMap<String, Module> modules = new HashMap<>();
  private final @NotNull Module bazaarModule = new BazaarModule();
  private final @NotNull Module chatModule = new ChatModule();
  private final @NotNull Module chatgamesModule = new ChatgamesModule();
  private final @NotNull Module discordModule = new DiscordModule();
  private final @NotNull Module economyModule = new EconomyModule();
  private final @NotNull Module homeModule = new HomeModule();
  private final @NotNull Module inventoryModule = new InventoryModule();
  private final @NotNull Module restartModule = new RestartModule();
  private final @NotNull Module rtpModule = new RtpModule();
  private final @NotNull Module skipnightModule = new SkipNightModule();
  private final @NotNull Module spawnModule = new SpawnModule();
  private final @NotNull Module staffModule = new StaffModule();
  private final @NotNull Module warpModule = new WarpModule();
  
  
  @Override
  public void onEnable() {
    
    INSTANCE = this;
    
    this.loadLangConfig();
    
    userData = UserUtils.loadData();
    
    registerListeners();
    
    TeleportUtils.startTeleportTask();
    
    modules.put(bazaarModule.getId(), bazaarModule);
    modules.put(chatModule.getId(), chatModule);
    modules.put(chatgamesModule.getId(), chatgamesModule);
    modules.put(discordModule.getId(), discordModule);
    modules.put(homeModule.getId(), homeModule);
    modules.put(inventoryModule.getId(), inventoryModule);
    modules.put(economyModule.getId(), economyModule);
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
        startupLogEmbed.addField(new MessageEmbed.Field(v.getId() + " module", "Enabled", false));
        v.initialize();
        v.registerCommands();
        v.registerListeners();
      } else {
        startupLogEmbed.addField(new MessageEmbed.Field(v.getId() + " module", "Disabled", false));
        log(Level.INFO, "Module " + v.getId() + " is disabled.");
      }
    });
    
    DiscordModule.discordManager.sendEmbed(DiscordModule.discordManager.LOGGING_CHANNEL, startupLogEmbed.build());
    
    
  }
  
  @Override
  public void onDisable() {
    DiscordModule.discordManager.sendEmbed(DiscordModule.discordManager.LOGGING_CHANNEL, new EmbedBuilder().setTitle("**Core Plugin Disabled**").build());
    UserUtils.saveData(userData);
    DiscordModule.discordManager.getJda().shutdown();
  }
  
  public static @Nonnull Main getInstance() {
    return INSTANCE;
  }
  
  public static void log(Level level, String message) {
    Main.getInstance().getLogger().log(level, "[core]: " + message);
  }
  
  public static @Nonnull Map<UUID, User> getUserData() {
    return userData;
  }
  
  public static User getUser(UUID uuid) {
    return userData.get(uuid);
  }
  
  public static @NotNull PluginManager getPluginManager() {
    return INSTANCE.getServer().getPluginManager();
  }
  
  private void loadLangConfig() {
    File langFile = new File(getDataFolder(), "lang.yml");
    if (!langFile.exists()) {
      try {
        getDataFolder().mkdir();
        langFile.createNewFile();
        InputStream defConfigStream = this.getResource("lang.yml");
        if (defConfigStream != null) {
          YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
          defConfig.save(langFile);
          Lang.setFile(defConfig);
        }
      } catch (IOException e) {
        e.printStackTrace();
        Main.getInstance().getServer().getLogger().severe("Couldn't create language file.");
        Main.getInstance().getServer().getLogger().severe("This is a fatal error. Now disabling");
        this.onDisable();
      }
    }
    YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
    for (Lang item : Lang.values()) {
      if (conf.getString(item.getPath()) == null) {
        conf.set(item.getPath(), item.getDefault());
      }
    }
    Lang.setFile(conf);
    try {
      conf.save(langFile);
    } catch (IOException e) {
      Main.getInstance().getServer().getLogger().log(Level.WARNING, "Failed to save lang.yml.");
      Main.getInstance().getServer().getLogger().log(Level.WARNING, "Report this stack trace to the plugin creator.");
      e.printStackTrace();
    }
  }
  
  private void registerListeners() {
    new onPlayerJoin();
    
    TeleportUtils.PlayerTeleportCancelListener listener = new TeleportUtils.PlayerTeleportCancelListener();
    this.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
  }
}
