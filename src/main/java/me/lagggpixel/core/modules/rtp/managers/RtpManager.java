package me.lagggpixel.core.modules.rtp.managers;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class RtpManager {
  
  private static final Random random = new Random();
  private static final @NotNull World world = Objects.requireNonNull(Bukkit.getWorld("world"));
  private static final int maxAttempts = 50;
  
  public static void teleportRandomly(Player player) {
    
    final double worldBorderCenterX = world.getWorldBorder().getCenter().getX();
    final double worldBorderCenterZ = world.getWorldBorder().getCenter().getZ();
    final double worldBorderSize = world.getWorldBorder().getSize() / 2.0;
    
    Location randomLocation;
    int attempts = 0;
    
    do {
      double x = worldBorderCenterX - worldBorderSize + random.nextDouble() * worldBorderSize * 2;
      double z = worldBorderCenterZ - worldBorderSize + random.nextDouble() * worldBorderSize * 2;
      
      double y = player.getWorld().getHighestBlockYAt((int) x, (int) z);
      
      randomLocation = new Location(player.getWorld(), x, y, z);
      
      attempts++;
      player.sendMessage(Lang.RTP_ATTEMPTING_TO_FIND_LOCATION.toComponentWithPrefix(Map.of(
          "%num%", String.valueOf(attempts),
          "%max%", String.valueOf(maxAttempts)
      )));
      
      if (attempts > maxAttempts) {
        player.sendMessage(Lang.RTP_NO_SAFE_LOCATION.toComponentWithPrefix(null));
        return;
      }
    } while (!isSafeLocation(randomLocation) || randomLocation.getY() <= 60);
    
    TeleportUtils.teleportWithDelay(player, randomLocation, "a random location");
  }
  
  private static boolean isSafeLocation(@NotNull Location location) {
    Block block = location.getBlock();
    Material blockType = block.getType();
    
    return !blockType.equals(Material.LAVA) && !blockType.equals(Material.WATER);
  }
}
