package me.lagggpixel.core.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class DelayTeleport {

    Player player;
    Location location;
    int default_delay = 5;
    int current_delay;
    String place_name;

    public DelayTeleport(Player player, Location location, int current_delay, String place_name) {
        this.player = player;
        this.location = location;
        this.current_delay = current_delay;
        this.place_name = place_name;
    }

    public DelayTeleport(Player player, Location location, String place_name) {
        this.player = player;
        this.location = location;
        this.current_delay = default_delay;
        this.place_name = place_name;
    }

    public void minus_delay() {
        this.current_delay--;
    }

}
