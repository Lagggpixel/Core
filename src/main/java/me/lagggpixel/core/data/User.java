package me.lagggpixel.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.modules.home.data.Home;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class User implements ConfigurationSerializable {
    @NotNull
    private final UUID playerUUID;

    @NotNull
    private String playerName;

    private Map<String, Home> homes;

    private double playerBalance;

    private boolean afk = false;

    private boolean isVanished;

    /**
     * Constructs a new user.
     *
     * @param player        The player object.
     */
    public User(@NotNull Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerBalance = 0.00;
        this.homes = new HashMap<>();
        afk = false;
        isVanished = false;
    }

    public User(Map<String, Object> map) {
        this.playerName = String.valueOf(map.get("name"));
        this.playerUUID = UUID.fromString(String.valueOf(map.get("uuid")));

        this.playerBalance = Double.parseDouble(String.valueOf(map.get("balance")));
        // noinspection unchecked
        this.homes = (Map<String, Home>) map.get("homes");

        this.isVanished = (boolean) map.get("vanished");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return new HashMap<>() {{
            put("name", playerName);
            put("uuid", playerUUID.toString());

            put("balance", playerBalance);
            put("homes", homes);

            put("vanished", isVanished);
        }};
    }
}
