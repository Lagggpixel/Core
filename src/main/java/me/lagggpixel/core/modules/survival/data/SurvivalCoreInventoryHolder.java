package me.lagggpixel.core.modules.survival.data;

import me.lagggpixel.core.data.CoreInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class SurvivalCoreInventoryHolder extends CoreInventoryHolder {
  protected SurvivalCoreInventoryHolder(Player player, Component title, int slots) {
    super(player, title, slots);
  }
  
  protected SurvivalCoreInventoryHolder(Player player, String title, int slots) {
    super(player, title, slots);
  }
}
