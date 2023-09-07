package me.lagggpixel.core.data;

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

public class User {
    @NotNull
    private final UUID playerUUID;

    @NotNull
    private String playerName;

    @Nullable
    private Component playerDisplayName;

    @NotNull
    private Double playerBalance;

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
    }

    /**
     * Constructs a new user.
     *
     * @param playerUUID        The UUID of the player.
     * @param playerName        The name of the player.
     * @param playerDisplayName The display name of the player.
     * @param playerBalance     The balance of the player.
     * @param homes             The homes that the player has.
     */
    public User(@NotNull UUID playerUUID, @NotNull String playerName, @Nullable Component playerDisplayName, @NotNull Double playerBalance, @NotNull Map<String, Home> homes) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.playerDisplayName = playerDisplayName;
        this.playerBalance = playerBalance;
        this.homes = homes;
    }

    // Getter and setter methods as before

    /**
     * Gets the homes associated with the user.
     *
     * @return A map of home names to their corresponding Home objects.
     */
    @NotNull
    public Map<String, Home> getHomes() {
        return homes;
    }

    /**
     * Sets the homes associated with the user.
     *
     * @param homes A map of home names to their corresponding Home objects.
     */
    public void setHomes(@NotNull Map<String, Home> homes) {
        this.homes = homes;
    }

    /**
     * Adds a new home for the user.
     *
     * @param home The home to add.
     */
    public void addHome(@NotNull Home home) {
        homes.put(home.getName(), home);
    }

    /**
     * Removes a home associated with the user.
     *
     * @param homeName The name of the home to remove.
     */
    public void removeHome(@NotNull String homeName) {
        homes.remove(homeName);
    }

    public @NotNull UUID getPlayerUUID() {
        return playerUUID;
    }

    public @NotNull String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(@NotNull String playerName) {
        this.playerName = playerName;
    }

    public @Nullable Component getPlayerDisplayName() {
        return playerDisplayName;
    }

    public void setPlayerDisplayName(@Nullable Component playerDisplayName) {
        if (playerDisplayName != null && Bukkit.getOfflinePlayer(this.playerUUID).isOnline()) {Objects.requireNonNull(Bukkit.getPlayer(this.playerUUID)).displayName(playerDisplayName);
        }
        this.playerDisplayName = playerDisplayName;
    }

    public @NotNull Double getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(@NotNull Double playerBalance) {
        this.playerBalance = playerBalance;
    }
}
