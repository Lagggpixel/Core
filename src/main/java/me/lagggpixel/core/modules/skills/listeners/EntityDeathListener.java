package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

  private final SkillsModule skillsModule;

  public EntityDeathListener(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void EntityDeathEvent(EntityDeathEvent event) {
    Player player = event.getEntity().getKiller();
    if (player == null) {
      return;
    }
    Entity entity = event.getEntity();
    if (skillsModule.getSkillHandler().isMobFarming(entity)) {
      handleFarmingMobKill();
    }

    if (skillsModule.getSkillHandler().isMobMining(entity)) {
      handleMiningMobKill();
    }

    if (skillsModule.getSkillHandler().isMobCombat(entity)) {
      handleCombatMobKill();
    }
  }

  private void handleFarmingMobKill() {
  }

  private void handleMiningMobKill() {
  }

  private void handleCombatMobKill() {
  }
}
