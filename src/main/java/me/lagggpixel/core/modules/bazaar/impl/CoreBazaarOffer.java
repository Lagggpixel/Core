package me.lagggpixel.core.modules.bazaar.impl;

import lombok.Data;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarOffer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class CoreBazaarOffer implements BazaarOffer, ConfigurationSerializable {
  
  private final UUID owner;
  private final int amount;
  private final double price;
  
  @Override
  public @NotNull Map<String, Object> serialize() {
    HashMap<String, Object> map = new HashMap<>();
    
    map.put("owner", this.owner.toString());
    map.put("amount", this.amount);
    map.put("price", this.price);
    
    return map;
  }
  
  public CoreBazaarOffer deserialize(Map<String, Object> map) {
    return new CoreBazaarOffer(
        UUID.fromString((String) map.get("owner")),
        (int) map.get("amount"),
        (double) map.get("price")
    );
  }
}
