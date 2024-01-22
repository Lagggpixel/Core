/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.listeners.PlayerStatsListeners;
import me.lagggpixel.core.listeners.onPlayerJoin;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.bz.category.CategoryConfiguration;
import me.lagggpixel.core.modules.bazaar.bz.product.ProductConfiguration;
import me.lagggpixel.core.modules.bazaar.bz.productcategory.ProductCategoryConfiguration;
import me.lagggpixel.core.modules.bazaar.menu.DefaultConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.menu.configurations.*;
import me.lagggpixel.core.modules.chat.ChatModule;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.economy.EconomyModule;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.home.HomeModule;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.modules.survival.SurvivalModule;
import me.lagggpixel.core.modules.warp.WarpModule;
import me.lagggpixel.core.serializers.UserDataSerializer;
import me.lagggpixel.core.utils.HologramUtils;
import me.lagggpixel.core.utils.LangUtils;
import me.lagggpixel.core.utils.TeleportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public final class Main extends JavaPlugin {
  
  private static Main INSTANCE;
  public boolean whitelisted;
  public NamespacedKey itemTag;
  private static Map<UUID, User> userData;
  
  static {
    ConfigurationSerialization.registerClass(InstantPlayerData.class);
    ConfigurationSerialization.registerClass(Home.class);
    
    // Bazaar
    ConfigurationSerialization.registerClass(ProductConfiguration.class);
    ConfigurationSerialization.registerClass(ProductCategoryConfiguration.class);
    ConfigurationSerialization.registerClass(CategoryConfiguration.class);
    ConfigurationSerialization.registerClass(DefaultConfigurableMenuItem.class);
    ConfigurationSerialization.registerClass(CategoryMenuConfiguration.class);
    ConfigurationSerialization.registerClass(ProductCategoryMenuConfiguration.class);
    ConfigurationSerialization.registerClass(SearchMenuConfiguration.class);
    ConfigurationSerialization.registerClass(ProductMenuConfiguration.class);
    ConfigurationSerialization.registerClass(ConfirmationMenuConfiguration.class);
    ConfigurationSerialization.registerClass(OrdersMenuConfiguration.class);
  }
  
  private final @NotNull HashMap<String, IModule> modules = new HashMap<>();
  private final @NotNull IModule bazaarModule = new BazaarModule();
  private final @NotNull IModule chatModule = new ChatModule();
  private final @NotNull IModule discordModule = new DiscordModule();
  private final @NotNull IModule economyModule = new EconomyModule();
  private final @NotNull IModule guildModule = new GuildModule();
  private final @NotNull IModule homeModule = new HomeModule();
  private final @NotNull IModule merchantModule = new MerchantModule();
  private final @NotNull IModule skillModule = new SkillsModule();
  private final @NotNull IModule skipnightModule = new SkipNightModule();
  private final @NotNull IModule spawnModule = new SpawnModule();
  private final @NotNull IModule staffModule = new StaffModule();
  private final @NotNull IModule warpModule = new WarpModule();
  private final @NotNull IModule survivalModule = new SurvivalModule();
  @Setter
  @Getter
  private Logger log4jLogger;
  
  public static @NotNull Main getInstance() {
    return INSTANCE;
  }
  
  public static void log(Level level, String message) {
    Main.getInstance().getLogger().log(level, "[Infinite Minecrafters Core]: " + message);
  }
  
  public static @NotNull Map<UUID, User> getUserData() {
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
    this.saveDefaultConfig();
    this.saveConfig();
    this.whitelisted = getConfig().getBoolean("whitelisted", true);
    itemTag = new NamespacedKey(Main.getInstance(), "itemTag");
    
    LangUtils.loadLangConfig();
    
    userData = UserDataSerializer.loadData();
    
    registerListeners();
    registerCommands();
    
    TeleportUtils.startTeleportTask();
    
    modules.put(bazaarModule.getId(), bazaarModule);
    modules.put(chatModule.getId(), chatModule);
    modules.put(discordModule.getId(), discordModule);
    modules.put(homeModule.getId(), homeModule);
    modules.put(merchantModule.getId(), merchantModule);
    modules.put(skillModule.getId(), skillModule);
    modules.put(economyModule.getId(), economyModule);
    modules.put(guildModule.getId(), guildModule);
    modules.put(skipnightModule.getId(), skipnightModule);
    modules.put(spawnModule.getId(), spawnModule);
    modules.put(staffModule.getId(), staffModule);
    modules.put(warpModule.getId(), warpModule);
    modules.put(survivalModule.getId(), survivalModule);
    
    EmbedBuilder startupLogEmbed = new EmbedBuilder();
    startupLogEmbed.setTitle("**Core Plugin Started**");
    
    modules.forEach((k, v) -> {
      if (v.isEnabled()) {
        log(Level.INFO, "IModule " + v.getId() + " is enabled.");
        startupLogEmbed.addField(StringUtils.capitalize(v.getId()) + " module", "Enabled", true);
        v.onEnable();
        v.registerCommands();
        v.registerListeners();
      } else {
        startupLogEmbed.addField(StringUtils.capitalize(v.getId()) + " module", "Disabled", true);
        log(Level.INFO, "IModule " + v.getId() + " is disabled.");
      }
    });
    
    DiscordModule.discordHandler.sendEmbed(DiscordModule.discordHandler.LOGGING_CHANNEL, startupLogEmbed);
  }
  
  @Override
  public void onDisable() {
    modules.forEach((k, v) -> {
      if (v.isEnabled()) {
        v.onDisable();
      }
    });
    UserDataSerializer.saveData(userData);
    HologramUtils.despawnAll();
  }
  
  private void registerListeners() {
    new onPlayerJoin();
    new PlayerStatsListeners();
    
    TeleportUtils.PlayerTeleportCancelListener listener = new TeleportUtils.PlayerTeleportCancelListener();
    this.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
  }
  
  private void registerCommands() {
  }
}
