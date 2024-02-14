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
import me.lagggpixel.core.modules.discord.tickets.TicketHandler;
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.ExceptionUtils;
import me.lagggpixel.core.utils.FileUtil;
import me.lagggpixel.core.utils.HookUtils;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
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
import java.util.regex.Pattern;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
public class DiscordHandler {

  private File configFile;
  @Getter
  private final YamlConfiguration yamlConfiguration;

  @Getter
  private static DiscordHandler instance;
  @NotNull
  public final ServerTextChannel CONSOLE_CHANNEL;
  @NotNull
  public final ServerTextChannel MESSAGING_CHANNEL;
  @NotNull
  public final ServerTextChannel LOGGING_CHANNEL;
  @NotNull
  public final Server server;

  @Getter
  private final DiscordApi discordApi;

  public Optional<ServerTextChannel> TICKET_LOG_CHANNEL;

  public DiscordHandler() {
    if (instance != null) {
      throw new RuntimeException("DiscordHandler is already initialized! DiscordHandler is a singleton!");
    }
    instance = this;
    configFile = new File(Main.getInstance().getDataFolder() + "/data/modules/discord", "discord.yml");
    if (!configFile.exists()) {
      FileUtil.copyToDefault("data/modules/discord/discord.yml");
      configFile = new File(Main.getInstance().getDataFolder() + "/data/modules/discord", "discord.yml");
    }
    yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
    String token = yamlConfiguration.getString("token");
    if (StringUtils.isBlank(token)) {
      throw new RuntimeException("Discord Token is not set! The plugin will not work correctly!");
    }
    discordApi = new DiscordApiBuilder()
        .setToken(token)
        .setAllIntents()
        .login().join();

    Optional<Server> optionalServer = discordApi.getServerById(yamlConfiguration.getString("serverId"));
    if (optionalServer.isEmpty()) {
      throw new RuntimeException("Discord server is not set correctly! The plugin will not work correctly!");
    }
    this.server = optionalServer.get();

    Optional<ServerTextChannel> optionalServerTextChannel;

    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("messageChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      throw new RuntimeException("Discord message channel is not set correctly! The plugin will not work correctly!");
    }
    MESSAGING_CHANNEL = optionalServerTextChannel.get();

    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("consoleChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      throw new RuntimeException("Discord console channel is not set correctly! The plugin will not work correctly!");
    }
    CONSOLE_CHANNEL = optionalServerTextChannel.get();

    optionalServerTextChannel = discordApi.getServerTextChannelById(yamlConfiguration.getString("loggingChannelId"));
    if (optionalServerTextChannel.isEmpty()) {
      throw new RuntimeException("Discord logging channel is not set correctly! The plugin will not work correctly!");
    }
    LOGGING_CHANNEL = optionalServerTextChannel.get();

    TICKET_LOG_CHANNEL = discordApi.getServerTextChannelById(yamlConfiguration.getString("ticketLogChannelId"));

    new SlashCommandRegistry();
    new TicketHandler();

    if (yamlConfiguration.contains("startMessage")) {
      new BukkitRunnable() {
        @Override
        public void run() {
          MESSAGING_CHANNEL.sendMessage(yamlConfiguration.getString("startMessage")).join();
        }
      }.runTaskLater(Main.getInstance(), 0L);
    }
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

  // <editor-fold defaultstate="collapsed" desc="Chat handling">
  public void handleAsyncChatEvent(@NotNull AsyncChatEvent event) {
    Player player = event.getPlayer();
    Component message =event.message();
    String playerDisplayName = ChatUtils.componentToString(player.displayName());
    String messageString = ChatUtils.componentToString(message);
    String compiledMessage = playerDisplayName + ": " + messageString;
    MESSAGING_CHANNEL.sendMessage(compiledMessage).join();
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
  public String getAvatarUrl(@NotNull OfflinePlayer player) {
    UUID uuid = player.getUniqueId();
    return "https://mc-heads.net/avatar/" + uuid + "/100/noHelm";
  }

  public String getAvatarUrl(@NotNull Player player) {
    UUID uuid = player.getUniqueId();
    return "https://mc-heads.net/avatar/" + uuid + "/100/noHelm";
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

  public void saveConfig() {
    try {
      yamlConfiguration.save(configFile);
    } catch (Exception e) {
      ExceptionUtils.handleException(e);
    }
  }
}
