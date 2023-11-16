package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StaffModeHandler {
  
  private final HashMap<UUID, ArmorStand> staffModeArmorStands = new HashMap<>();
  
  public void enterStaffMode(Player player) {
    User user = Main.getUser(player.getUniqueId());
    
    if (user.isStaffMode()) {
      return;
    }
    
    user.setStaffMode(true);
    InstantPlayerData instantPlayerData = new InstantPlayerData(player);
    user.setInstantPlayerData(instantPlayerData);
    player.setGameMode(GameMode.CREATIVE);
    player.setSaturation(20);
    player.setHealth(20);
    player.setFoodLevel(20);
    player.setFlying(true);
    player.setAffectsSpawning(false);
    
    ArmorStand as = player.getLocation().getWorld().spawn(player.getLocation().add(0, 2.5, 0), ArmorStand.class);
    staffModeArmorStands.put(player.getUniqueId(), as);
    
    as.setGravity(false);
    as.setCanPickupItems(false);
    as.customName(ChatUtils.stringToComponent("Staff Mode").color(TextColor.color(Color.GRAY.asRGB())));
    as.setCustomNameVisible(true);
    as.setVisible(false);
    player.addPassenger(as);
  }
  
  public void exitStaffMode(Player player) {
    User user = Main.getUser(player.getUniqueId());
    
    if (!user.isStaffMode()) {
      return;
    }
    
    ArmorStand as = staffModeArmorStands.get(player.getUniqueId());
    player.removePassenger(as);
    as.remove();
    staffModeArmorStands.remove(player.getUniqueId());
    
    user.setStaffMode(false);
    InstantPlayerData instantPlayerData = user.getInstantPlayerData();
    instantPlayerData.restorePlayerData(player);
    user.setInstantPlayerData(null);
  }
  
}
