package me.lagggpixel.core.modules.merchant.handler;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.merchant.data.Merchant;
import me.lagggpixel.core.modules.merchant.data.MerchantItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MerchantHandler {
  private final MerchantSellPriceHandler merchantSellPriceHandler;
  
  @Getter
  private final HashMap<String, Merchant> merchants;
  
  public MerchantHandler() {
    this.merchants = new HashMap<>();
    
    this.merchantSellPriceHandler = new MerchantSellPriceHandler();
    
    this.registerMerchants();
  }
  
  public void registerMerchants() {
    File file = new File(Main.getInstance().getDataFolder() + "/module_data/merchant", "merchants.yml");
    if (!file.exists()) {
      new File(Main.getInstance().getDataFolder() + "/module_data/merchant", "merchants.yml");
      return;
    }
    
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    for (String uniqueId : yamlConfiguration.getKeys(false)) {
      ConfigurationSection section = yamlConfiguration.getConfigurationSection(uniqueId);
      assert section != null;
      String name = section.getString("name");
      String id = section.getString("id");
      
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
        
        merchantItems.add(new MerchantItem(material, cost));
      }
      
      Location location = section.getLocation("location");
      
      this.registerMerchant(id, new Merchant(
          name,
          skinValue,
          skinSignature,
          merchantItems,
          location,
          Objects.equals(id, "librarian_merchant"),
          Objects.equals(id, "librarian_merchant") ? Villager.Profession.LIBRARIAN : null
      ));
    }
  }
  
  public void registerMerchant(String id, Merchant merchant) {
    this.merchants.put(id, merchant);

    Bukkit.getPluginManager().registerEvents(merchant, Main.getInstance());
  }
  
  public Merchant getMerchant(String name) {
    return this.merchants.get(name);
  }
  
  public MerchantSellPriceHandler getPriceHandler() {
    return merchantSellPriceHandler;
  }
}