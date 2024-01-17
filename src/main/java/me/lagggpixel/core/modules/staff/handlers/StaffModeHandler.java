/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

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

    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
  }
  
  public void exitStaffMode(Player player) {
    User user = Main.getUser(player.getUniqueId());
    user.setStaffMode(false);
    InstantPlayerData instantPlayerData = user.getInstantPlayerData();
    instantPlayerData.restorePlayerData(player);
    user.setInstantPlayerData(null);
  }
  
}
