package me.lagggpixel.core.modules.staff.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class InstantPlayerData implements ConfigurationSerializable {
  
  double health;
  int foodLevel;
  float saturation;
  
  GameMode gameMode;
  boolean isFlying;
  boolean isSleepingIgnored;
  boolean affectsSpawning;
  
  int totalExperience;
  
  ItemStack[] armour;
  ItemStack[] inventory;
  
  public InstantPlayerData(Player player) {
    this.health = player.getHealth();
    this.foodLevel = player.getFoodLevel();
    this.saturation = player.getSaturation();
    
    this.gameMode = player.getGameMode();
    this.isFlying = player.isFlying();
    this.isSleepingIgnored = player.isSleepingIgnored();
    this.affectsSpawning = player.getAffectsSpawning();
    
    totalExperience = player.getTotalExperience();
    
    PlayerInventory playerInventory = player.getInventory();
    this.armour = playerInventory.getArmorContents();
    this.inventory = playerInventory.getStorageContents();
  }
  
  public void restorePlayerData(Player player) {
    player.setHealth(health);
    player.setFoodLevel(foodLevel);
    player.setSaturation(saturation);
    
    player.setGameMode(gameMode);
    player.setFlying(isFlying);
    player.setSleepingIgnored(isSleepingIgnored);
    player.setAffectsSpawning(affectsSpawning);
    
    player.setTotalExperience(totalExperience);
    
    player.getInventory().setArmorContents(armour);
    player.getInventory().setContents(inventory);
    
    PlayerInventory playerInventory = player.getInventory();
    playerInventory.setArmorContents(armour);
    playerInventory.setContents(inventory);
  }
  
  
  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> data = new HashMap<>();
    
    data.put("health", health);
    data.put("foodLevel", foodLevel);
    data.put("saturation", saturation);
    data.put("gameMode", gameMode.toString());
    data.put("isFlying", isFlying);
    data.put("affectsSpawning", affectsSpawning);
    data.put("isSleepingIgnored", isSleepingIgnored);
    data.put("totalExperience", totalExperience);
    
    data.put("armour", serializeItemStackArray(armour));
    data.put("inventory", serializeItemStackArray(inventory));
    
    return data;
  }
  
  private List<Map<String, Object>> serializeItemStackArray(ItemStack[] itemStacks) {
    List<Map<String, Object>> serializedArray = new ArrayList<>();
    
    for (ItemStack itemStack : itemStacks) {
      if (itemStack != null) {
        serializedArray.add(itemStack.serialize());
      } else {
        serializedArray.add(null);
      }
    }
    
    return serializedArray;
  }
  
  // Add a static method for deserialization
  @NotNull
  public static InstantPlayerData deserialize(Map<String, Object> map) {
    InstantPlayerData instantPlayerData = new InstantPlayerData();
    if (map == null || map.isEmpty()) {
      return instantPlayerData;
    }
    instantPlayerData.health = Double.parseDouble(String.valueOf(map.get("health")));
    instantPlayerData.foodLevel = Integer.parseInt(String.valueOf(map.get("foodLevel")));
    instantPlayerData.saturation = Float.parseFloat(String.valueOf(map.get("saturation")));
    instantPlayerData.gameMode = GameMode.valueOf((String) map.get("gameMode"));
    instantPlayerData.isFlying = Boolean.parseBoolean(String.valueOf(map.get("isFlying")));
    instantPlayerData.affectsSpawning = Boolean.parseBoolean(String.valueOf(map.get("affectsSpawning")));
    instantPlayerData.isSleepingIgnored = Boolean.parseBoolean(String.valueOf(map.get("isSleepingIgnored")));
    instantPlayerData.totalExperience = Integer.parseInt(String.valueOf(map.get("totalExperience")));
    
    instantPlayerData.armour = deserializeItemStackArray((List<Map<String, Object>>) map.get("armour"));
    instantPlayerData.inventory = deserializeItemStackArray((List<Map<String, Object>>) map.get("inventory"));
    
    return instantPlayerData;
  }
  
  private static ItemStack[] deserializeItemStackArray(List<Map<String, Object>> serializedArray) {
    ItemStack[] itemStacks = new ItemStack[serializedArray.size()];
    
    for (int i = 0; i < serializedArray.size(); i++) {
      if (serializedArray.get(i) != null) {
        itemStacks[i] = ItemStack.deserialize(serializedArray.get(i));
      } else {
        itemStacks[i] = null;
      }
    }
    
    return itemStacks;
  }
}