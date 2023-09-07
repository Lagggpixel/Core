package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.Lang;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TeleportUtils {

    private static int TELEPORT_DELAY = 5;

    private static final Map<Player, BukkitRunnable> teleportTasks = new HashMap<>();

    public static Map<Player, BukkitRunnable> getTeleportTasks() {
        return teleportTasks;
    }

    public static void teleportWithDelay(Player player, Location location) {
        BukkitRunnable teleportTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline() && player.getLocation().equals(location)) {
                    player.teleport(location);
                }
                teleportTasks.remove(player);
            }
        };

        teleportTasks.put(player, teleportTask);

        teleportTask.runTaskLater(Main.getInstance(), TELEPORT_DELAY * 20L);
    }

    public static void cancelTeleport(Player player, String reason) {
        // Check if there is a teleport task for the player and cancel it
        BukkitRunnable teleportTask = teleportTasks.get(player);
        if (teleportTask != null) {
            teleportTask.cancel();
            player.sendMessage(Lang.TELEPORTATION_CANCELED.toComponentWithPrefix(Map.of(
                    "%reason%", reason
            )));
            teleportTasks.remove(player);
        }
    }


    public static class PlayerTeleportCancelListener implements Listener {

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {
            if (teleportTasks.containsKey(event.getPlayer())) {
                cancelTeleport(event.getPlayer(), "moving");
            }
        }

        @EventHandler
        public void onEntityDamage(EntityDamageEvent event) {
            if (event.getEntity() instanceof Player player) {
                if (teleportTasks.containsKey(player)) {
                    cancelTeleport(player, "taking damage");
                }
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            if (teleportTasks.containsKey(event.getPlayer())) {
                cancelTeleport(event.getPlayer(), "logging out");
            }
        }
    }
}