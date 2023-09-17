package me.lagggpixel.core.utils;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.DelayTeleport;
import me.lagggpixel.core.data.Lang;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeleportUtils {

    public static void startTeleportTask() {
        BukkitRunnable teleportRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> playersToRemove = new ArrayList<>();

                if (!teleportTasks.isEmpty()) {
                    teleportTasks.forEach((k, v) -> {
                        if (k == null || !k.isOnline()) {
                            playersToRemove.add(k);
                        } else {
                            if (v.getCurrent_delay() == 0) {
                                k.teleport(v.getLocation());
                                k.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of(
                                        "%name%", v.getPlace_name()
                                )));
                                playersToRemove.add(k);
                            } else if (v.getCurrent_delay() < 0) {
                                playersToRemove.add(k);
                            } else {
                                k.sendMessage(Lang.TELEPORTATION_IN_TIME.toComponentWithPrefix(Map.of(
                                        "%time%", String.valueOf(v.getCurrent_delay())
                                )));
                                v.minus_delay();
                            }
                        }
                    });

                    // Remove the players outside the loop to avoid ConcurrentModificationException
                    playersToRemove.forEach(teleportTasks::remove);
                }
            }
        };
        teleportRunnable.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    @Getter
    private static final Map<Player, DelayTeleport> teleportTasks = new HashMap<>();

    public static void teleportWithDelay(Player player, Location location, String place_name) {
        if (!teleportTasks.containsKey(player)) teleportTasks.put(player, new DelayTeleport(player, location, place_name));
        else teleportTasks.replace(player, new DelayTeleport(player, location, place_name));
    }


    public static void cancelTeleport(Player player, String reason) {
        DelayTeleport teleportTask = teleportTasks.get(player);
        teleportTasks.remove(player, teleportTask);

        player.sendMessage(Lang.TELEPORTATION_CANCELED.toComponentWithPrefix(Map.of(
                "%reason%", reason
        )));
    }


    public static class PlayerTeleportCancelListener implements Listener {

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {
            if (teleportTasks.containsKey(event.getPlayer())) {
                if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() ||
                event.getFrom().getZ() != event.getTo().getZ()) {
                    cancelTeleport(event.getPlayer(), "moving");
                }
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