package me.lagggpixel.core.modules.staff.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.data.InstantPlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class StaffModeHandler {
  
  public void enterStaffMode(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setStaffMode(true);
    InstantPlayerData instantPlayerData = new InstantPlayerData(player);
    user.setInstantPlayerData(instantPlayerData);
    player.setGameMode(GameMode.CREATIVE);
    player.setSaturation(20);
    player.setHealth(20);
    player.setFoodLevel(20);
    player.setFlying(true);
    player.setAffectsSpawning(false);
  }
  
  public void exitStaffMode(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setStaffMode(false);
    InstantPlayerData instantPlayerData = user.getInstantPlayerData();
    instantPlayerData.restorePlayerData(player);
    user.setInstantPlayerData(null);
  }
  
}
