/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.FileUtil;
import me.lagggpixel.core.utils.HookUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
@SuppressWarnings({"DataFlowIssue", "FieldCanBeLocal"})
public class DiscordHandler {
  
  private File configFile;
  @Getter
  private final YamlConfiguration yamlConfiguration;
  
  @Getter
  private static DiscordHandler instance;
  @NotNull
  private final NMSHandler nmsHandler;
  @NotNull
  public ServerTextChannel CONSOLE_CHANNEL = null;
  @NotNull
  public ServerTextChannel MESSAGING_CHANNEL = null;
  @NotNull
  public ServerTextChannel LOGGING_CHANNEL = null;
  @NotNull
  public Server server = null;
  
  @Getter
  private final DiscordApi discordApi;
  
  public DiscordHandler(@NotNull NMSHandler nmsHandler) {
    if (instance != null) {
      throw new RuntimeException("DiscordHandler is already initialized! DiscordHandler is a singleton!");
    }
    instance = this;
    this.nmsHandler = nmsHandler;
    configFile = new File(Main.getInstance().getDataFolder() + "/data/modules/discord", "discord.yml");
    if (!configFile.exists()) {
      FileUtil.copyToDefault("data/modules/discord/discord.yml");
      configFile = new File(Main.getInstance().getDataFolder() + "/data/modules/discord", "discord.yml");
    }
    yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
    String token = yamlConfiguration.getString("token");
    if (StringUtils.isBlank(token)) {
      Main.log(Level.SEVERE, "Discord token is not set, disabling Core!");
      Main.getInstance().onDisable();
    }
    discordApi = new DiscordApiBuilder()
        .setToken(token)
        .setAllIntents()
        .login().join();
    
    Optional<Server> optionalServer = discordApi.getServerById(yamlConfiguration.getString("serverId"));
    if (optionalServer.isEmpty()) {
      Main.log(Level.SEVERE, "Discord server is not set correctly, disabling Core!");
      Main.getInstance().onDisable();
      return;
    }
    this.server = optionalServer.get();
    
    Optional<ServerTextChannel> optionalServerTextChannel;
    
    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("messageChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      Main.log(Level.SEVERE, "Discord message channel is not set correctly, disabling Core!");
      Main.getInstance().onDisable();
      return;
    }
    MESSAGING_CHANNEL = optionalServerTextChannel.get();
    
    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("consoleChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      Main.log(Level.SEVERE, "Discord console channel is not set correctly, disabling Core!");
      Main.getInstance().onDisable();
      return;
    }
    CONSOLE_CHANNEL = optionalServerTextChannel.get();
    
    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("loggingChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      Main.log(Level.SEVERE, "Discord logging channel is not set correctly, disabling Core!");
      Main.getInstance().onDisable();
      return;
    }
    LOGGING_CHANNEL = optionalServerTextChannel.get();
  }
  
  public Optional<User> getMemberById(String id) {
    return server.getMemberById(id);
  }
  
  public void sendEmbed(@NotNull TextChannel textChannel, @NotNull EmbedBuilder embed) {
    textChannel.sendMessage(embed).join();
  }
  
  // <editor-fold defaultstate="collapsed" desc="Join/Quit embeds">
  public @NotNull EmbedBuilder createJoinMessageEmbed(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String message = player.getName() + " joined the server";
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.GREEN);
  }
  
  public @NotNull EmbedBuilder createQuitMessageEmbed(@NotNull PlayerQuitEvent event) {
    
    Player player = event.getPlayer();
    String message = player.getName() + " left the server";
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.RED);
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Chat embed handling">
  public @NotNull EmbedBuilder createChatEmbed(@NotNull AsyncChatEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("**" + PlainTextComponentSerializer.plainText().serialize(event.message()) + "**")
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.YELLOW);
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Death embed handling">
  public @NotNull EmbedBuilder createDeathMessageEmbed(@NotNull PlayerDeathEvent event) {
    
    Player player = event.getPlayer();
    String message = ChatUtils.componentToString(event.deathMessage());
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.MAGENTA);
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Guilds embed handling">
  public @NotNull EmbedBuilder createGuildCreatedEmbed(@NotNull GuildCreateEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("Guild '" + event.getGuild().getName() + "' created successfully by " + player.getName())
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.BLUE);
  }
  
  public @NotNull EmbedBuilder createGuildDisbandEmbed(@NotNull GuildDisbandEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("Guild '" + event.getGuild().getName() + "' disbanded successfully by " + player.getName())
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.BLUE);
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Avatar URL">
  @Deprecated
  public String getAvatarUrlDeprecated(String username, UUID uuid) {
    String avatarUrl = constructAvatarUrlDeprecated(username, uuid, "");
    avatarUrl = replacePlaceholdersToDiscord(avatarUrl);
    return avatarUrl;
  }
  
  public String getAvatarUrl(@NotNull OfflinePlayer player) {
    UUID uuid = player.getUniqueId();
    return "https://mc-heads.net/avatar/" + uuid + "/100/noHelm";
  }
  
  @Deprecated
  public String getAvatarUrlDeprecated(@NotNull OfflinePlayer player) {
    if (player.isOnline() && player.getPlayer() != null) {
      return getAvatarUrl(player.getPlayer());
    } else {
      String avatarUrl = constructAvatarUrlDeprecated(player.getName(), player.getUniqueId(), "");
      avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
      return avatarUrl;
    }
  }
  
  public String getAvatarUrl(@NotNull Player player) {
    UUID uuid = player.getUniqueId();
    return "https://mc-heads.net/avatar/" + uuid + "/100/noHelm";
  }
  
  @Deprecated
  public String getAvatarUrlDeprecated(@NotNull Player player) {
    String avatarUrl = constructAvatarUrlDeprecated(player.getName(), player.getUniqueId(), nmsHandler.getTexture(player));
    avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
    return avatarUrl;
  }
  
  @Deprecated
  private @NotNull String constructAvatarUrlDeprecated(String username, UUID uuid, String texture) {
    OfflinePlayer player = null;
    if (StringUtils.isBlank(username) && uuid != null) {
      player = Bukkit.getOfflinePlayer(uuid);
    }
    if (StringUtils.isBlank(texture) && player != null && player.isOnline()) {
      texture = nmsHandler.getTexture(player.getPlayer());
    }
    
    String avatarUrl = "https://crafatar.com/avatars/{uuid-nodashes}.png?size={size}&overlay#{texture}";
    
    
    avatarUrl = avatarUrl
        .replace("{texture}", texture != null ? texture : "")
        .replace("{uuid-nodashes}", uuid.toString().replace("-", ""))
        .replace("{size}", "128");
    
    return avatarUrl;
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Utils">
  private String strip(String text) {
    return stripLegacy(text);
  }
  
  private String stripLegacy(String text) {
    if (StringUtils.isBlank(text)) {
      return "";
    }
    
    return Pattern.compile("(?<!<@)[&§\u007F](?i)[0-9a-fklmnorx]").matcher(text).replaceAll("");
  }
  
  private String replacePlaceholdersToDiscord(String input, OfflinePlayer player) {
    boolean placeholderapi = HookUtils.pluginHookIsEnabled("placeholderapi");
    
    if (placeholderapi) input = input.replace("&", "&\u200B");
    
    input = replacePlaceholders(input, player);
    
    if (placeholderapi) {
      input = stripLegacySectionOnly(input);
      input = input.replace("&\u200B", "&");
    }
    return input;
  }
  
  private String replacePlaceholdersToDiscord(String input) {
    return replacePlaceholdersToDiscord(input, null);
  }
  
  private String stripLegacySectionOnly(String text) {
    return Pattern.compile("(?<!<@)§(?i)[0-9a-fklmnorx]").matcher(text).replaceAll("");
  }
  
  private String replacePlaceholders(String input, OfflinePlayer player) {
    if (input == null) return null;
    if (HookUtils.pluginHookIsEnabled("placeholderapi")) {
      Player onlinePlayer = player != null ? player.getPlayer() : null;
      input = PlaceholderAPI.setPlaceholders(
          onlinePlayer != null ? onlinePlayer : player, input);
    }
    return input;
  }
  // </editor-fold>
}
