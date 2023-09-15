package me.lagggpixel.core.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.homes.data.Home;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Data
@Getter
@Setter
public class User {
    @NotNull
    private final UUID playerUUID;

    @NotNull
    private String playerName;

    @NotNull
    private Boolean isVanished;

    @Nullable
    private Component playerDisplayName;

    @NotNull
    private Double playerBalance;

    @NotNull
    private Boolean afk;

    private Map<String, Home> homes;

    /**
     * Constructs a new user.
     *
     * @param player        The player object.
     */
    public User(@NotNull Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerDisplayName = player.displayName();
        this.playerBalance = 0.00;
        this.homes = new HashMap<>();
        afk = false;
        isVanished = false;
    }

    /**
     * Constructs a new user.
     *
     * @param playerUUID        The UUID of the player.
     * @param playerName        The name of the player.
     * @param playerDisplayName The display name of the player.
     * @param playerBalance     The balance of the player.
     * @param isVanished        If the player is vanished or not
     * @param homes             The homes that the player has.
     */
    public User(@NotNull UUID playerUUID, @NotNull String playerName, @Nullable Component playerDisplayName, @NotNull Double playerBalance,
                @NotNull Boolean afk, @NotNull Boolean isVanished,
                @NotNull Map<String, Home> homes) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.playerDisplayName = playerDisplayName;
        this.playerBalance = playerBalance;
        this.homes = homes;
        this.isVanished = isVanished;
        this.afk = afk;
    }

    public void setPlayerDisplayName(@Nullable Component playerDisplayName) {
        if (playerDisplayName != null && Bukkit.getOfflinePlayer(this.playerUUID).isOnline()) {Objects.requireNonNull(Bukkit.getPlayer(this.playerUUID)).displayName(playerDisplayName);
        }
        this.playerDisplayName = playerDisplayName;
    }
}
