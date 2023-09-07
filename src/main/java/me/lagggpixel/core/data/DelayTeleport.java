package me.lagggpixel.core.data;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class DelayTeleport {

    Player player;
    Location location;
    int default_delay = 5;
    int current_delay;

    public DelayTeleport(Player player, Location location, int current_delay) {
        this.player = player;
        this.location = location;
        this.current_delay = current_delay;
    }

    public DelayTeleport(Player player, Location location) {
        this.player = player;
        this.location = location;
        this.current_delay = default_delay;
    }

    public void minus_delay() {
        this.current_delay--;
    }

}
