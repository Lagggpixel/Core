package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skills.enums.SkillExpGainCause;
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
    User user = Main.getUser(player.getUniqueId());
    Entity entity = event.getEntity();
    if (skillsModule.getSkillHandler().isMobFarming(entity)) {
      handleFarmingMobKill(user, entity);
    }

    if (skillsModule.getSkillHandler().isMobMining(entity)) {
      handleMiningMobKill(user, entity);
    }

    if (skillsModule.getSkillHandler().isMobCombat(entity)) {
      handleCombatMobKill(user, entity);
    }
  }

  private void handleFarmingMobKill(User user, Entity entity) {
    double exp = skillsModule.getSkillHandler().getFarmingEntities().get(entity.getType());
    user.getSkills().getFarming().addExp(exp, SkillExpGainCause.MOB_KILL);
  }

  private void handleMiningMobKill(User user, Entity entity) {
    double exp = skillsModule.getSkillHandler().getMiningEntities().get(entity.getType());
    user.getSkills().getMining().addExp(exp, SkillExpGainCause.MOB_KILL);
  }

  private void handleCombatMobKill(User user, Entity entity) {
    double exp = skillsModule.getSkillHandler().getCombatEntities().get(entity.getType());
    user.getSkills().getCombat().addExp(exp, SkillExpGainCause.MOB_KILL);
  }
}
