/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.economy.managers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.economy.events.BalanceTopUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class EconomyManager {

  private static EconomyManager INSTANCE;
  private final List<Map.Entry<UUID, Double>> balanceTop;
  
  public EconomyManager() {
    this.balanceTop = new ArrayList<>();
    
    BukkitRunnable runnable = new BukkitRunnable() {
      @Override
      public void run() {
        updateBalanceTop();
      }
    };
    
    runnable.runTaskTimer(Main.getInstance(), 0L, 12000L);
  }
  
  public void setBalanceTop(@NotNull Map<UUID, Double> map) {
    this.balanceTop.clear();
    this.balanceTop.addAll(map.entrySet());
  }
  
  @NotNull
  public List<Map.Entry<UUID, Double>> getBalanceTop() {
    return this.balanceTop;
  }
  
  @Deprecated
  public boolean withdraw(String name, double amount) {
    if (this.getBalance(name) >= amount) {
      this.setBalance(name, this.getBalance(name) - amount);
      return true;
    }
    return false;
  }
  
  public boolean withdraw(@NotNull OfflinePlayer player, double amount) {
    return this.withdraw(player.getUniqueId(), amount);
  }
  
  public boolean withdraw(@NotNull UUID uuid, double amount) {
    if (this.getBalance(uuid) >= amount) {
      this.setBalance(uuid, this.getBalance(uuid) - amount);
      return true;
    }
    return false;
  }
  
  @Deprecated
  public boolean deposit(String name, double amount) {
    this.setBalance(name, this.getBalance(name) + amount);
    return true;
  }
  
  public boolean deposit(@NotNull OfflinePlayer player, double amount) {
    return this.deposit(player.getUniqueId(), amount);
  }
  
  public boolean deposit(@NotNull UUID uuid, double amount) {
    this.setBalance(uuid, this.getBalance(uuid) + amount);
    return true;
  }
  
  @Deprecated
  public void setBalance(String name, double amount) {
    User user = Main.getUser(Objects.requireNonNull(Bukkit.getPlayerUniqueId(name)));

    this.setBalance(user, Math.max(0, amount));
  }
  
  public void setBalance(@NotNull OfflinePlayer player, double amount) {
    this.setBalance(player.getUniqueId(), amount);
  }
  
  public void setBalance(@NotNull UUID uuid, double amount) {
    User user = Main.getUser(uuid);

    this.setBalance(user, Math.max(0, amount));
  }
  
  public void setBalance(@NotNull User user, double amount) {
    user.setPlayerBalance(amount);
  }
  
  @Deprecated
  public double getBalance(String name) {
    User user = Main.getUser(Objects.requireNonNull(Bukkit.getPlayerUniqueId(name)));
    return this.getBalance(user);
  }
  
  public double getBalance(@NotNull OfflinePlayer player) {
    return this.getBalance(player.getUniqueId());
  }
  
  public double getBalance(@NotNull UUID uuid) {
    User user = Main.getUser(uuid);
    return this.getBalance(user);
  }
  
  public double getBalance(@NotNull User user) {
    return user.getPlayerBalance();
  }
  
  @Deprecated
  public boolean hasEnough(String name, double amount) {
    User user = Main.getUser(Objects.requireNonNull(Bukkit.getPlayerUniqueId(name)));

    return amount < this.getBalance(user);
  }
  
  public boolean hasEnough(@NotNull OfflinePlayer player, double amount) {
    return this.hasEnough(player.getUniqueId(), amount);
  }
  
  public boolean hasEnough(@NotNull UUID uuid, double amount) {
    User user = Main.getUser(uuid);

    return amount < this.getBalance(user);
  }
  
  @Deprecated
  public boolean hasAccount(String name) {
    return true;
  }
  
  public boolean hasAccount(@NotNull OfflinePlayer player) {
    return this.hasAccount(player.getUniqueId());
  }
  
  public boolean hasAccount(@NotNull UUID uuid) {
    return true;
  }
  
  public void updateBalanceTop() {
    long timeStarted = System.currentTimeMillis();
    
    Map<UUID, User> userMap = Main.getUserData();
    Map<UUID, Double> tempBalanceMap = new HashMap<>();
    
    Map<UUID, Double> finalTempBalanceMap = tempBalanceMap;
    userMap.forEach((uuid, user) -> finalTempBalanceMap.put(uuid, user.getPlayerBalance()));
    
    tempBalanceMap = sortBalanceMap(tempBalanceMap);
    setBalanceTop(tempBalanceMap);

    BalanceTopUpdateEvent event = new BalanceTopUpdateEvent(timeStarted, System.currentTimeMillis());
    Bukkit.getPluginManager().callEvent(event);
  }
  
  private Map<UUID, Double> sortBalanceMap(Map<UUID, Double> unsortedMap) {
    Stream<Map.Entry<UUID, Double>> entryStream = unsortedMap.entrySet().stream();
    
    List<Map.Entry<UUID, Double>> sortedEntries = entryStream
        .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
        .toList();
    
    Map<UUID, Double> sortedMap = new LinkedHashMap<>();
    for (Map.Entry<UUID, Double> entry : sortedEntries) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    
    return sortedMap;
  }

  public static EconomyManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new EconomyManager();
    }
    return INSTANCE;
  }
}