/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.DelayTeleport;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.enums.Lang;
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

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class TeleportUtils {

  public static final String TELEPORTATION_BYPASS_PERMISSION = "core.teleportation.bypass";

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
              if (v.getCurrentDelay() == 0) {
                k.teleport(v.getLocation());
                k.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of(
                    "%name%", v.getPlaceName()
                )));
                playersToRemove.add(k);
              } else if (v.getCurrentDelay() < 0) {
                playersToRemove.add(k);
              } else {
                k.sendMessage(Lang.TELEPORTATION_IN_TIME.toComponentWithPrefix(Map.of(
                    "%time%", String.valueOf(v.getCurrentDelay())
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
    User user = Main.getUser(player.getUniqueId());
    if (player.hasPermission(TELEPORTATION_BYPASS_PERMISSION)) {
      player.teleport(location);
      user.sendMessage(Lang.TELEPORTATION_SUCCESS.toComponentWithPrefix(Map.of(
          "%name%", place_name
      )));
      return;
    }

    if (!teleportTasks.containsKey(player)) teleportTasks.put(player, new DelayTeleport(player, location, place_name));
    else teleportTasks.replace(player, new DelayTeleport(player, location, place_name));
  }


  public static void cancelTeleport(Player player, String reason) {
    User user = Main.getUser(player.getUniqueId());
    DelayTeleport teleportTask = teleportTasks.get(player);
    teleportTasks.remove(player, teleportTask);

    user.sendMessage(Lang.TELEPORTATION_CANCELED.toComponentWithPrefix(Map.of(
        "%reason%", reason
    )));
  }


  public static class PlayerTeleportCancelListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
      if (teleportTasks.containsKey(event.getPlayer())) {
        DelayTeleport teleportTask = teleportTasks.get(event.getPlayer());
        if (Math.abs(Math.round(teleportTask.getLocation().getX() - event.getFrom().getX())) > 0.5
            || Math.abs(Math.round(teleportTask.getLocation().getZ() - event.getFrom().getZ())) > 0.5
            || Math.abs(Math.round(teleportTask.getLocation().getY() - event.getFrom().getY())) > 0.5) {
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