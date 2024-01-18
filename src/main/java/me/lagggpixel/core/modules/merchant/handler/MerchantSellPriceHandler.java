/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.merchant.handler;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.utils.FileUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class MerchantSellPriceHandler {
  
  private final HashMap<Material, Double> prices;
  
  public MerchantSellPriceHandler() {
    this.prices = new HashMap<>();
    
    init();
  }
  
  private void init() {
    File file = new File(Main.getInstance().getDataFolder() + "/data/merchant", "prices.yml");
    
    if (!file.exists()) {
      FileUtil.copyToDefault("data/merchant/prices.yml");
      file = new File(Main.getInstance().getDataFolder() + "/data/merchant", "prices.yml");
    }
    
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    for (String key : yamlConfiguration.getKeys(false)) {
      Material material = Material.matchMaterial(key);
      if (material == null) {
        continue;
      }
      if (prices.containsKey(material)) {
        continue;
      }
      double price = yamlConfiguration.getDouble(key);
      prices.put(material, price);
    }
  }
  
  public double getPrice(@NotNull ItemStack item) {
    Material material = item.getType();
    if (!prices.containsKey(material)) {
      return 0.0;
    }
    return prices.get(material);
  }
}