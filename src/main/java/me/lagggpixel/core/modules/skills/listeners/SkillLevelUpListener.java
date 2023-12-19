package me.lagggpixel.core.modules.skills.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.modules.skills.events.SkillLevelUpEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;

public class SkillLevelUpListener implements Listener {

  private final SkillsModule module;

  public SkillLevelUpListener(SkillsModule module) {
    this.module = module;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void SkillLevelUpEvent(SkillLevelUpEvent event) {
    Component component = Lang.SKILL_LEVEL_UP.toComponent(Map.of("%skill%", event.getSkillType().getName(),
        "%level_before%", String.valueOf(event.getLevel() - 1),
        "%level_after%", String.valueOf(event.getLevel()),
        "%coins%", String.valueOf(event.getReward())));

    Main.getUser(event.getUuid()).sendMessage(component);
  }
}
