package me.lagggpixel.core.utils;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class DiscordUtils {

    private final static String CONSOLE_CHANNEL_ID = "1152691818916487348";

    public static JDA jda;


    public static void initialize() {
        jda = JDABuilder.createDefault("MTE1MjU5ODM3ODIwMzU4NjY5NQ.GBogbS.tKNCHVoS5Qa8N4DWH9EgMlwbfw7a7qQsZVIiMw").build();
    }

    public static MessageEmbed createEmbed() {
        return new EmbedBuilder()
                .setTitle()
                .build();
    }

    public static void sendEmbed(TextChannel textChannel, String title, String message) {
        textChannel.sendMessageEmbeds()
    }

    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        // return if advancement or player objects are knackered because this can apparently happen for some reason
        if (event.getAdvancement() == null || player == null) return;

        // don't send messages for advancements related to recipes
        String key = event.getAdvancement().getKey().getKey();
        if (key.contains("recipe/") || key.contains("recipes/")) return;

        // respect invisibility plugins
        if (PlayerUtil.isVanished(player)) return;

        // ensure advancements should be announced in the world
        World world = player.getWorld();
        Boolean isGamerule = GAMERULE_CLASS_AVAILABLE // This class was added in 1.13
                ? world.getGameRuleValue((GameRule<Boolean>) GAMERULE) // 1.13+
                : Boolean.parseBoolean(world.getGameRuleValue((String) GAMERULE)); // <= 1.12
        if (Boolean.FALSE.equals(isGamerule)) return;

        Bukkit.getScheduler().runTaskAsynchronously(DiscordSRV.getPlugin(), () -> runAsync(event));
    }

    private void runAsync(PlayerAdvancementDoneEvent event) {

        String channelName = DiscordSRV.getPlugin().getOptionalChannel("awards");
        Player player = event.getPlayer();
        Advancement advancement = event.getAdvancement();

        MessageFormat messageFormat = DiscordSRV.getPlugin().getMessageFromConfiguration("MinecraftPlayerAchievementMessage");
        if (messageFormat == null) return;

        // turn "story/advancement_name" into "Advancement Name"
        String advancementTitle = getTitle(advancement);

        AchievementMessagePreProcessEvent preEvent = DiscordSRV.api.callEvent(new AchievementMessagePreProcessEvent(channelName, messageFormat, player, advancementTitle, event));
        if (preEvent.isCancelled()) {
            DiscordSRV.debug(Debug.MINECRAFT_TO_DISCORD, "AchievementMessagePreProcessEvent was cancelled, message send aborted");
            return;
        }
        // Update from event in case any listeners modified parameters
        advancementTitle = preEvent.getAchievementName();
        channelName = preEvent.getChannel();
        messageFormat = preEvent.getMessageFormat();

        if (messageFormat == null) return;

        String finalAchievementName = StringUtils.isNotBlank(advancementTitle) ? advancementTitle : "";
        String avatarUrl = DiscordSRV.getAvatarUrl(player);
        String botAvatarUrl = DiscordUtil.getJda().getSelfUser().getEffectiveAvatarUrl();
        String botName = DiscordSRV.getPlugin().getMainGuild() != null ? DiscordSRV.getPlugin().getMainGuild().getSelfMember().getEffectiveName() : DiscordUtil.getJda().getSelfUser().getName();
        String displayName = StringUtils.isNotBlank(player.getDisplayName()) ? MessageUtil.strip(player.getDisplayName()) : "";

        TextChannel destinationChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelName);
        BiFunction<String, Boolean, String> translator = (content, needsEscape) -> {
            if (content == null) return null;
            content = content
                    .replaceAll("%time%|%date%", TimeUtil.timeStamp())
                    .replace("%username%", needsEscape ? DiscordUtil.escapeMarkdown(player.getName()) : player.getName())
                    .replace("%displayname%", needsEscape ? DiscordUtil.escapeMarkdown(displayName) : displayName)
                    .replace("%usernamenoescapes%", player.getName())
                    .replace("%displaynamenoescapes%", displayName)
                    .replace("%world%", player.getWorld().getName())
                    .replace("%achievement%", MessageUtil.strip(needsEscape ? DiscordUtil.escapeMarkdown(finalAchievementName) : finalAchievementName))
                    .replace("%embedavatarurl%", avatarUrl)
                    .replace("%botavatarurl%", botAvatarUrl)
                    .replace("%botname%", botName);
            if (destinationChannel != null) content = DiscordUtil.translateEmotes(content, destinationChannel.getGuild());
            content = PlaceholderUtil.replacePlaceholdersToDiscord(content, player);
            return content;
        };
        Message discordMessage = DiscordSRV.translateMessage(messageFormat, translator);
        if (discordMessage == null) return;

        String webhookName = translator.apply(messageFormat.getWebhookName(), false);
        String webhookAvatarUrl = translator.apply(messageFormat.getWebhookAvatarUrl(), false);

        AchievementMessagePostProcessEvent postEvent = DiscordSRV.api.callEvent(new AchievementMessagePostProcessEvent(channelName, discordMessage, player, advancementTitle, event, messageFormat.isUseWebhooks(), webhookName, webhookAvatarUrl, preEvent.isCancelled()));
        if (postEvent.isCancelled()) {
            DiscordSRV.debug(Debug.MINECRAFT_TO_DISCORD, "AchievementMessagePostProcessEvent was cancelled, message send aborted");
            return;
        }
        // Update from event in case any listeners modified parameters
        channelName = postEvent.getChannel();
        discordMessage = postEvent.getDiscordMessage();

        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelName);
        if (postEvent.isUsingWebhooks()) {
            WebhookUtil.deliverMessage(textChannel, postEvent.getWebhookName(), postEvent.getWebhookAvatarUrl(),
                    discordMessage.getContentRaw(), discordMessage.getEmbeds().stream().findFirst().orElse(null));
        } else {
            DiscordUtil.queueMessage(textChannel, discordMessage, true);
        }
    }


    public static String getAvatarUrl(String username, UUID uuid) {
        String avatarUrl = constructAvatarUrl(username, uuid, "");
        avatarUrl = PlaceholderUtil.replacePlaceholdersToDiscord(avatarUrl);
        return avatarUrl;
    }
    private static String getAvatarUrl(OfflinePlayer player) {
        if (player.isOnline()) {
            return getAvatarUrl(player.getPlayer());
        } else {
            String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), "");
            avatarUrl = PlaceholderUtil.replacePlaceholdersToDiscord(avatarUrl, player);
            return avatarUrl;
        }
    }
    public static String getAvatarUrl(Player player) {
        String avatarUrl = constructAvatarUrl(player.getName(), player.getUniqueId(), NMSUtil.getTexture(player));
        avatarUrl = PlaceholderUtil.replacePlaceholdersToDiscord(avatarUrl, player);
        return avatarUrl;
    }

    private static String constructAvatarUrl(String username, UUID uuid, String texture) {
        boolean offline = uuid == null || uuidIsOffline(uuid);
        OfflinePlayer player = null;
        if (StringUtils.isNotBlank(username) && offline) {
            // resolve username to player/uuid
            //TODO resolve name to online uuid when offline player is present
            // (can't do it by calling Bukkit.getOfflinePlayer(username).getUniqueId() because bukkit just returns the offline-mode CraftPlayer)
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
            texture = NMSUtil.getTexture(player.getPlayer());
        }

        String avatarUrl = DiscordSRV.config().getString("AvatarUrl");
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

            if (!offlineUuidAvatarUrlNagged) {
                offlineUuidAvatarUrlNagged = true;
            }
        }

        if (username.startsWith("*")) {
            // geyser adds * to beginning of it's usernames
            username = username.substring(1);
        }
        try {
            username = URLEncoder.encode(username, "utf8");
        } catch (UnsupportedEncodingException ignored) {}

        String usedBaseUrl = avatarUrl;
        avatarUrl = avatarUrl
                .replace("{texture}", texture != null ? texture : "")
                .replace("{username}", username)
                .replace("{uuid}", uuid != null ? uuid.toString() : "")
                .replace("{uuid-nodashes}", uuid.toString().replace("-", ""))
                .replace("{size}", "128");

        return avatarUrl;
    }

    public static boolean uuidIsOffline(UUID uuid) {
        return uuid.version() == 3;
    }

    public static String getTexture(Player player) {
        try {
            Object profile = NMSUtil.getGameProfile(player);
            if (profile == null) return null;
            Object propertyMap = NMSUtil.method_GameProfile_getProperties.invoke(profile);
            Object textureProperty = NMSUtil.getTextureProperty(propertyMap);
            if (textureProperty != null) {
                String textureB64 = (String) NMSUtil.field_GameProfileProperty_value.get(textureProperty);
                String textureData = new String(Base64.decodeBase64(textureB64));
                Matcher matcher = NMSUtil.TEXTURE_URL_PATTERN.matcher(textureData);
                if (matcher.find()) return matcher.group("texture");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String replacePlaceholdersToDiscord(String input, OfflinePlayer player) {
        boolean placeholderapi = HookUtils.pluginHookIsEnabled("placeholderapi");

        // PlaceholderAPI has a side effect of replacing chat colors at the end of placeholder conversion
        // that breaks role mentions: <@&role id> because it converts the & to a §
        // So we add a zero width space after the & to prevent it from translating, and remove it after conversion
        if (placeholderapi) input = input.replace("&", "&\u200B");

        input = replacePlaceholders(input, player);

        if (placeholderapi) {
            input = MessageUtil.stripLegacySectionOnly(input); // Color codes will be in this form
            input = input.replace("&\u200B", "&");
        }
        return input;
    }

    public static String replacePlaceholders(String input, OfflinePlayer player) {
        if (input == null) return null;
        if (HookUtils.pluginHookIsEnabled("placeholderapi")) {
            Player onlinePlayer = player != null ? player.getPlayer() : null;
            input = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
                    onlinePlayer != null ? onlinePlayer : player, input);
        }
        return input;
    }

    private class NMSUtil {
        private static final Pattern TEXTURE_URL_PATTERN = Pattern.compile("https?://.+(?<texture>\\w{64})\"");

        protected static String versionPrefix = "";
        protected static boolean failed = false;

        protected static Class<?> class_CraftPlayer;
        protected static Class<?> class_GameProfile;
        protected static Class<?> class_GameProfileProperty;
        protected static Class<?> class_EntityPlayer;
        protected static Method method_CraftPlayer_getHandle;
        protected static Method method_EntityPlayer_getGameProfile;
        protected static Method method_GameProfile_getProperties;
        protected static Field field_PropertyMap_properties;
        protected static Field field_GameProfileProperty_value;

        static {
            String className = Bukkit.getServer().getClass().getName();
            String[] packages = className.split("\\.");
            if (packages.length == 5) {
                versionPrefix = packages[3] + ".";
            }

            try {
                class_EntityPlayer = fixBukkitClass("net.minecraft.server.EntityPlayer", "net.minecraft.server.level.EntityPlayer");
                try {
                    method_EntityPlayer_getGameProfile = class_EntityPlayer.getMethod("getProfile");
                } catch (NoSuchMethodException e) {
                    try {
                        method_EntityPlayer_getGameProfile = class_EntityPlayer.getMethod("getGameProfile");
                    } catch (NoSuchMethodException e2) {
                        method_EntityPlayer_getGameProfile = Arrays.stream(class_EntityPlayer.getMethods())
                                .filter(method -> method.getReturnType().getSimpleName().equals("GameProfile"))
                                .findFirst().orElseThrow(() -> new RuntimeException("Couldn't find the GameProfile method"));
                    }
                }

                class_CraftPlayer = fixBukkitClass("org.bukkit.craftbukkit.entity.CraftPlayer");
                method_CraftPlayer_getHandle = class_CraftPlayer.getMethod("getHandle");

                class_GameProfile = getClass("com.mojang.authlib.GameProfile");
                class_GameProfileProperty = getClass("com.mojang.authlib.properties.Property");
                if (class_GameProfile == null) {
                    class_GameProfile = getClass("net.minecraft.util.com.mojang.authlib.GameProfile");
                    class_GameProfileProperty = getClass("net.minecraft.util.com.mojang.authlib.properties.Property");
                }
                method_GameProfile_getProperties = class_GameProfile.getMethod("getProperties");
                field_GameProfileProperty_value = class_GameProfileProperty.getDeclaredField("value");
                field_GameProfileProperty_value.setAccessible(true);
                field_PropertyMap_properties = method_GameProfile_getProperties.getReturnType().getDeclaredField("properties");
                field_PropertyMap_properties.setAccessible(true);
            } catch (Throwable e) {
                e.printStackTrace();
                failed = true;
            }
        }

        public static Class<?> getClass(String className) {
            Class<?> result = null;
            try {
                result = NMSUtil.class.getClassLoader().loadClass(className);
            } catch (Exception ignored) {}
            return result;
        }

        public static Class<?> fixBukkitClass(String className, String... alternateClassNames) throws ClassNotFoundException {
            List<String> classNames = new ArrayList<>();
            classNames.add(className);
            classNames.addAll(Arrays.asList(alternateClassNames));

            for (String name : classNames) {
                try {
                    // Try without prefix, Spigot 1.17
                    return NMSUtil.class.getClassLoader().loadClass(name);
                } catch (ClassNotFoundException ignored) {}

                if (!versionPrefix.isEmpty()) {
                    name = name.replace("org.bukkit.craftbukkit.", "org.bukkit.craftbukkit." + versionPrefix);
                    name = name.replace("net.minecraft.server.", "net.minecraft.server." + versionPrefix);
                }

                try {
                    return NMSUtil.class.getClassLoader().loadClass(name);
                } catch (ClassNotFoundException ignored) {}
            }
            throw new ClassNotFoundException("Could not find " + className);
        }

        public static Object getHandle(Player player) {
            if (failed) return null;

            try {
                return method_CraftPlayer_getHandle.invoke(player);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        public static Object getGameProfile(Player player) {
            if (failed) return null;

            Object handle = getHandle(player);
            if (handle != null) {
                try {
                    return method_EntityPlayer_getGameProfile.invoke(handle);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static Object getTextureProperty(Object propertyMap) {
            if (failed) return null;

            try {
                Object multi = NMSUtil.field_PropertyMap_properties.get(propertyMap);
                //noinspection rawtypes
                Iterator it = ((Iterable) multi.getClass()
                        .getMethod("get", Object.class)
                        .invoke(multi, "textures")).iterator();
                if (it.hasNext()) {
                    return it.next();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String getTexture(Player player) {
            if (failed) return null;

            try {
                Object profile = getGameProfile(player);
                if (profile == null) return null;
                Object propertyMap = method_GameProfile_getProperties.invoke(profile);
                Object textureProperty = getTextureProperty(propertyMap);
                if (textureProperty != null) {
                    String textureB64 = (String) field_GameProfileProperty_value.get(textureProperty);
                    String textureData = new String(Base64.decodeBase64(textureB64));
                    Matcher matcher = TEXTURE_URL_PATTERN.matcher(textureData);
                    if (matcher.find()) return matcher.group("texture");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

