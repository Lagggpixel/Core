package me.lagggpixel.core.modules.discord.managers;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.lagggpixel.core.utils.HookUtils;
import me.lagggpixel.core.utils.NMSUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.UUID;
import java.util.regex.Pattern;

public class DiscordManager {
    private final static String CONSOLE_CHANNEL_ID = "1152691818916487348";
    private final static String FEATURE_TESTING_ID = "1152311090332586045";

    public static JDA jda;

    public void initialize() {
        jda = JDABuilder.createDefault("MTAwMTU4MDA4NjE4Njc1NDIwOQ.GCSQD2.xpfFptXlfirUex4nO9DOx4gLJXClbLjYUvTvyk").build();
    }

    // "MTE1MjU5ODM3ODIwMzU4NjY5NQ.GBogbS.tKNCHVoS5Qa8N4DWH9EgMlwbfw7a7qQsZVIiMw"

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

    public @NotNull MessageEmbed createChatEmbed(@NotNull AsyncChatEvent event) {

        Player player = event.getPlayer();

        return new EmbedBuilder()
                .setAuthor(player.getName(), null, getAvatarUrl(player))
                .setDescription("**" + PlainTextComponentSerializer.plainText().serialize(event.message()) + "**")
                .setTimestamp(java.time.Instant.now())
                .setColor(Color.YELLOW)
                .build();
    }

    public void sendEmbed(MessageEmbed embed) {

        TextChannel textChannel = jda.getTextChannelById(FEATURE_TESTING_ID);

        assert textChannel != null;

        textChannel.sendMessageEmbeds(embed).queue();
    }


    private String getAvatarUrl(String username, UUID uuid) {
        String avatarUrl = constructAvatarUrl(username, uuid, "");
        avatarUrl = replacePlaceholdersToDiscord(avatarUrl);
        return avatarUrl;
    }

    private String getAvatarUrl(@NotNull OfflinePlayer player) {
        if (player.isOnline()) {
            return getAvatarUrl(player.getPlayer());
        } else {
            String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), "");
            avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
            return avatarUrl;
        }
    }

    private String getAvatarUrl(@NotNull Player player) {
        String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), NMSUtils.getTexture(player));
        avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
        return avatarUrl;
    }

    private @NotNull String constructAvatarUrl(String username, UUID uuid, String texture) {
        OfflinePlayer player = null;
        if (StringUtils.isBlank(username) && uuid != null) {
            player = Bukkit.getOfflinePlayer(uuid);
        }
        if (StringUtils.isBlank(texture) && player != null && player.isOnline()) {
            texture = NMSUtils.getTexture(player.getPlayer());
        }

        String avatarUrl = "https://crafatar.com/avatars/{uuid-nodashes}.png?size={size}&overlay#{texture}";


        avatarUrl = avatarUrl
                .replace("{texture}", texture != null ? texture : "")
                .replace("{uuid-nodashes}", uuid.toString().replace("-", ""))
                .replace("{size}", "128");

        return avatarUrl;
    }

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
}
