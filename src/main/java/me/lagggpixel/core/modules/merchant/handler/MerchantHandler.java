/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.merchant.handler;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.data.MerchantItem;
import me.lagggpixel.core.utils.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Exortions
 * @since January 22, 2024
 */
@SuppressWarnings("FieldCanBeLocal")
public class MerchantHandler {
  @Getter
  private final File merchantFile;
  @Getter
  private final YamlConfiguration merchantConfiguration;
  
  @Getter
  private final HashMap<String, Merchant> merchants;
  
  private final String defaultSkinValue = "ewogICJ0aW1lc3RhbXAiIDogMTY3NTcyMDkzMjc3NSwKICAicHJvZmlsZUlkIiA6ICJjYmYxNGIxMGJhNWU0NzgwYjIyNmFiNmQzOTUxODk4YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJFZ2d5QnV0dG9uMjQxMSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80YWNmZTY1NGVjNTZmYjQ1ZTVhMzA2YzJmOWYwOTE0Nzg1NGNiMjA0ZjQxOTE2M2EyOTc0N2VjODJjZjg4YzE5IgogICAgfQogIH0KfQ==";
  private final String defaultSkinSignature = "SJ3UgiyBz4IO+gihrH0wEn3yGydYYw0yiJr8PxFxjay9v6HthvJP6mYDpG2/TZMetVUtFaHPy6BV0ZBVMo0oNt8Wkpbxq5NhvUCwLKRXSZIC8vjJme1gcTMqwn29ZJoXltHpgAOq54K7SnrPEgFHdq3nS7HH11BGsVSVRp9Oo028S7bEwHZf+QRnMzf/MdN6BIWWUjDwyYtx6EFqOE5BAlqA9ZRfDPPjRAoNfgg2s9JbrHRR7ouP6C224SRwbD6HFi1EC2XjsfcNSFPCYmoZIUoNAXKkNusgeaDY22OqkZGEtw1uJ86SoaPPZ0gJiGOuaKrPK7fYcG+3pDf0GFl9Fsv1rqjHjBq1tR+1PeCKuzY26Ad8E0tMdQapGwP974HEoF1AuJCHMxx/sC2WPk6Brw845kUnrducENBhtXg4cVP4aHeKRKGEDc5FeAcJBo7Q2FTIKr/fJ+cjsDCAn6vudsAmQBU+PnXZQcj3RAkJDa+kJPwCsCFZqIrUR79iqkw0GuJuxlFSjxy3Nhl14zc9eyWz+zv1MuRROuFVx88NZfNJGPFjhRPt7CeF+AiQ72RAGgnX9zoDJkNUPvbi5uTSm8HafK9HCfEX9zibIetYUjP6OciRxk9JvmPven5F0c7XI6Ow793kZnLr6NW3JGIiUDpb4odCjla+uaMVv75H3xo=";
  
  public MerchantHandler() {
    this.merchantFile = new File(Main.getInstance().getDataFolder() + "/data/modules/merchant", "merchants.yml");
    if (!merchantFile.exists()) {
      new File(Main.getInstance().getDataFolder() + "/data/modules/merchant", "merchants.yml");
    }
    this.merchantConfiguration = YamlConfiguration.loadConfiguration(merchantFile);
    this.merchants = new HashMap<>();
    
    this.registerMerchants();
  }
  
  public void createMerchant(String uniqueId, Location location) {
    Merchant merchant = new Merchant(uniqueId, uniqueId, defaultSkinValue, defaultSkinSignature, new ArrayList<>(), location);
    this.merchants.put(uniqueId, merchant);
    
    this.saveMerchant(uniqueId, merchant);
    merchant.createNpc();
    
    Bukkit.getPluginManager().registerEvents(merchant, Main.getInstance());
  }
  
  public void saveMerchant(String uniqueId, @NotNull Merchant merchant) {
    ConfigurationSection section = merchantConfiguration.createSection(uniqueId);
    section.set("name", merchant.getName());
    
    section.set("skinValue", merchant.getSkinValue());
    section.set("skinSignature", merchant.getSkinSignature());
    
    ConfigurationSection items = section.createSection("items");
    for (MerchantItem item : merchant.getItems()) {
      ConfigurationSection itemSection = items.createSection(String.valueOf(item.getRawSlot()));
      itemSection.set("material", item.getMaterial().name());
      itemSection.set("cost", item.getCost());
    }
    
    section.set("location", merchant.getLocation());
    try {
      merchantConfiguration.save(merchantFile);
    } catch (IOException e) {
      ExceptionUtils.handleException(e);
    }
  }
  
  public void registerMerchants() {
    for (String uniqueId : merchantConfiguration.getKeys(false)) {
      ConfigurationSection section = merchantConfiguration.getConfigurationSection(uniqueId);
      assert section != null;
      String name = section.getString("name");
      
      String skinValue = section.getString("skinValue");
      String skinSignature = section.getString("skinSignature");
      
      List<MerchantItem> merchantItems = new ArrayList<>();
      
      ConfigurationSection items = section.getConfigurationSection("items");
      assert items != null;
      for (String slot : items.getKeys(false)) {
        ConfigurationSection item = items.getConfigurationSection(slot);
        assert item != null;
        String materialString = item.getString("material");
        if (materialString == null) {
          continue;
        }
        Material material = Material.matchMaterial(materialString);
        int cost = item.getInt("cost");
        
        merchantItems.add(new MerchantItem(material, cost, Integer.parseInt(slot)));
      }
      
      Location location = section.getLocation("location");
      
      this.registerMerchant(uniqueId, new Merchant(
          uniqueId,
          name,
          skinValue,
          skinSignature,
          merchantItems,
          location
      ));
    }
  }
  
  public void registerMerchant(String id, Merchant merchant) {
    this.merchants.put(id, merchant);
    
    Bukkit.getPluginManager().registerEvents(merchant, Main.getInstance());
  }
  
  public Merchant getMerchant(String name) {
    if (!this.merchants.containsKey(name)) {
      return null;
    }
    return this.merchants.get(name);
  }
  
  public boolean hasMerchant(String currentMerchant) {
    return this.merchants.containsKey(currentMerchant);
  }
}