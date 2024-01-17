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
import me.lagggpixel.core.modules.guilds.events.GuildCreateEvent;
import me.lagggpixel.core.modules.guilds.events.GuildDisbandEvent;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.HookUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class DiscordHandler {
  
  @Getter
  private static DiscordHandler instance;
  @NotNull
  private final NMSHandler nmsHandler;
  @NotNull
  public final TextChannel CONSOLE_CHANNEL;
  @NotNull
  public final TextChannel MESSAGING_CHANNEL;
  @NotNull
  public final TextChannel LOGGING_CHANNEL;
  
  @Getter
  private final JDA jda;
  @NotNull
  @Getter
  private final Guild guild;
  
  public DiscordHandler(@NotNull NMSHandler nmsHandler) {
    if (instance != null) {
      throw new RuntimeException("DiscordHandler is already initialized! DiscordHandler is a singleton!");
    }
    instance = this;
    this.nmsHandler = nmsHandler;
    try {
      jda = JDABuilder.createDefault("MTAwMTU4MDA4NjE4Njc1NDIwOQ.GCSQD2.xpfFptXlfirUex4nO9DOx4gLJXClbLjYUvTvyk").build().awaitReady();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    guild = Objects.requireNonNull(jda.getGuildById("773814795291328512"));
    MESSAGING_CHANNEL = Objects.requireNonNull(jda.getTextChannelById("1151605428371869696"));
    CONSOLE_CHANNEL = Objects.requireNonNull(jda.getTextChannelById("1152691818916487348"));
    LOGGING_CHANNEL = Objects.requireNonNull(jda.getTextChannelById("1157654059378036776"));
  }
  
  // ""MTE1MjU5ODM3ODIwMzU4NjY5NQ.GBogbS.tKNCHVoS5Qa8N4DWH9EgMlwbfw7a7qQsZVIiMw""


  public Member getMemberById(String id) {
    return guild.getMemberById(id);
  }
  
  public void sendEmbed(@NotNull TextChannel textChannel, @NotNull MessageEmbed embed) {
    textChannel.sendMessageEmbeds(embed).queue();
  }
  
  // <editor-fold defaultstate="collapsed" desc="Join/Quit embeds">
  public @NotNull MessageEmbed createJoinMessageEmbed(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String message = player.getName() + " joined the server";
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.GREEN)
        .build();
  }
  
  public @NotNull MessageEmbed createQuitMessageEmbed(@NotNull PlayerQuitEvent event) {
    
    Player player = event.getPlayer();
    String message = player.getName() + " left the server";
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.RED)
        .build();
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Chat embed handling">
  public @NotNull MessageEmbed createChatEmbed(@NotNull AsyncChatEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("**" + PlainTextComponentSerializer.plainText().serialize(event.message()) + "**")
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.YELLOW)
        .build();
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Death embed handling">
  public @NotNull MessageEmbed createDeathMessageEmbed(@NotNull PlayerDeathEvent event) {
    
    Player player = event.getPlayer();
    String message = ChatUtils.componentToString(event.deathMessage());
    
    return new EmbedBuilder()
        .setAuthor(message, null, getAvatarUrl(player))
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.MAGENTA)
        .build();
  }
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Guilds embed handling">
  public @NotNull MessageEmbed createGuildCreatedEmbed(@NotNull GuildCreateEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("Guild '" + event.getGuild().getName() + "' created successfully by " + player.getName())
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.BLUE)
        .build();
  }
  
  public @NotNull MessageEmbed createGuildDisbandEmbed(@NotNull GuildDisbandEvent event) {
    
    Player player = event.getPlayer();
    
    return new EmbedBuilder()
        .setAuthor(player.getName(), null, getAvatarUrl(player))
        .setDescription("Guild '" + event.getGuild().getName() + "' disbanded successfully by " + player.getName())
        .setTimestamp(java.time.Instant.now())
        .setColor(Color.BLUE)
        .build();
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
