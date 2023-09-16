package me.lagggpixel.core.utils;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class DiscordUtils {

    private final static String CONSOLE_CHANNEL_ID = "1152691818916487348";
    private final static String FEATURE_TESTING_ID = "1152311090332586045";

    public static JDA jda;

    public static void initialize() {
        jda = JDABuilder.createDefault("MTAwMTU4MDA4NjE4Njc1NDIwOQ.GCSQD2.xpfFptXlfirUex4nO9DOx4gLJXClbLjYUvTvyk").build();
    }

    // "MTE1MjU5ODM3ODIwMzU4NjY5NQ.GBogbS.tKNCHVoS5Qa8N4DWH9EgMlwbfw7a7qQsZVIiMw"

    public static @NotNull MessageEmbed createJoinMessageEmbed(@NotNull PlayerJoinEvent event) {

        Player player = event.getPlayer();
        String message = player.getName() + " joined the server";

        return new EmbedBuilder()
                .setAuthor(message, null, getAvatarUrl(player))
                .setTimestamp(java.time.Instant.now())
                .setColor(Color.GREEN)
                .build();
    }

    public static @NotNull MessageEmbed createQuitMessageEmbed(@NotNull PlayerQuitEvent event) {

        Player player = event.getPlayer();
        String message = player.getName() + " left the server";

        return new EmbedBuilder()
                .setAuthor(message, null, getAvatarUrl(player))
                .setTimestamp(java.time.Instant.now())
                .setColor(Color.RED)
                .build();
    }

    public static @NotNull MessageEmbed createChatEmbed(@NotNull AsyncChatEvent event) {

        Player player = event.getPlayer();

        return new EmbedBuilder()
                .setAuthor(player.getName(), null, getAvatarUrl(player))
                .setDescription("**" + PlainTextComponentSerializer.plainText().serialize(event.message()) + "**")
                .setTimestamp(java.time.Instant.now())
                .setColor(Color.YELLOW)
                .build();
    }

    public static void sendEmbed(MessageEmbed embed) {

        TextChannel textChannel = jda.getTextChannelById(FEATURE_TESTING_ID);

        assert textChannel != null;

        textChannel.sendMessageEmbeds(embed).queue();
    }


    private static String getAvatarUrl(String username, UUID uuid) {
        String avatarUrl = constructAvatarUrl(username, uuid, "");
        avatarUrl = replacePlaceholdersToDiscord(avatarUrl);
        return avatarUrl;
    }

    private static String getAvatarUrl(OfflinePlayer player) {
        if (player.isOnline()) {
            return getAvatarUrl(player.getPlayer());
        } else {
            String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), "");
            avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
            return avatarUrl;
        }
    }

    private static String getAvatarUrl(Player player) {
        String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), NMSUtils.getTexture(player));
        avatarUrl = replacePlaceholdersToDiscord(avatarUrl, player);
        return avatarUrl;
    }

    private static String constructAvatarUrl(String username, UUID uuid, String texture) {
        boolean offline = uuid == null || uuidIsOffline(uuid);
        OfflinePlayer player = null;
        if (StringUtils.isNotBlank(username) && offline) {
            player = Bukkit.getOfflinePlayer(username);
            uuid = player.getUniqueId();
            offline = uuidIsOffline(uuid);
        }
        if (StringUtils.isBlank(username) && uuid != null) {
            // resolve uuid to player/username
            player = Bukkit.getOfflinePlayer(uuid);
            username = player.getName();
        }
        if (StringUtils.isBlank(texture) && player != null && player.isOnline()) {
            // grab texture placeholder from player if online
            texture = NMSUtils.getTexture(player.getPlayer());
        }

        String avatarUrl = "https://crafatar.com/avatars/{uuid-nodashes}.png?size={size}&overlay#{texture}";
        String defaultUrl = "https://crafatar.com/avatars/{uuid-nodashes}.png?size={size}&overlay#{texture}";
        String offlineUrl = "https://cravatar.eu/helmavatar/{username}/{size}.png#{texture}";

        if (StringUtils.isBlank(avatarUrl)) {
            avatarUrl = !offline ? defaultUrl : offlineUrl;
        }

        if (offline && !avatarUrl.contains("{username}")) {
            boolean defaultValue = avatarUrl.equals(defaultUrl);
            if (defaultValue) {
                // Using default value while in offline mode -> use offline url
                avatarUrl = offlineUrl;
            }

        }

        if (username.startsWith("*")) {
            // geyser adds * to beginning of it's usernames
            username = username.substring(1);
        }
        try {
            username = URLEncoder.encode(username, "utf8");
        } catch (UnsupportedEncodingException ignored) {
        }

        String usedBaseUrl = avatarUrl;
        avatarUrl = avatarUrl
                .replace("{texture}", texture != null ? texture : "")
                .replace("{username}", username)
                .replace("{uuid}", uuid != null ? uuid.toString() : "")
                .replace("{uuid-nodashes}", uuid.toString().replace("-", ""))
                .replace("{size}", "128");

        return avatarUrl;
    }

    private static String strip(String text) {
        return stripLegacy(text);
    }

    private static String stripLegacy(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        return Pattern.compile("(?<!<@)[&§\u007F](?i)[0-9a-fklmnorx]").matcher(text).replaceAll("");
    }

    private static boolean uuidIsOffline(UUID uuid) {
        return uuid.version() == 3;
    }

    private static String getTexture(Player player) {
        try {
            Object profile = NMSUtils.getGameProfile(player);
            if (profile == null) return null;
            Object propertyMap = NMSUtils.method_GameProfile_getProperties.invoke(profile);
            Object textureProperty = NMSUtils.getTextureProperty(propertyMap);
            if (textureProperty != null) {
                String textureB64 = (String) NMSUtils.field_GameProfileProperty_value.get(textureProperty);
                String textureData = new String(Base64.decodeBase64(textureB64));
                Matcher matcher = NMSUtils.TEXTURE_URL_PATTERN.matcher(textureData);
                if (matcher.find()) return matcher.group("texture");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String replacePlaceholdersToDiscord(String input, OfflinePlayer player) {
        boolean placeholderapi = HookUtils.pluginHookIsEnabled("placeholderapi");

        // PlaceholderAPI has a side effect of replacing chat colors at the end of placeholder conversion
        // that breaks role mentions: <@&role id> because it converts the & to a §
        // So we add a zero width space after the & to prevent it from translating, and remove it after conversion
        if (placeholderapi) input = input.replace("&", "&\u200B");

        input = replacePlaceholders(input, player);

        if (placeholderapi) {
            input = stripLegacySectionOnly(input); // Color codes will be in this form
            input = input.replace("&\u200B", "&");
        }
        return input;
    }

    private static String replacePlaceholdersToDiscord(String input) {
        return replacePlaceholdersToDiscord(input, null);
    }

    private static String stripLegacySectionOnly(String text) {
        return Pattern.compile("(?<!<@)§(?i)[0-9a-fklmnorx]").matcher(text).replaceAll("");
    }

    private static String replacePlaceholders(String input, OfflinePlayer player) {
        if (input == null) return null;
        if (HookUtils.pluginHookIsEnabled("placeholderapi")) {
            Player onlinePlayer = player != null ? player.getPlayer() : null;
            input = PlaceholderAPI.setPlaceholders(
                    onlinePlayer != null ? onlinePlayer : player, input);
        }
        return input;
    }
}

