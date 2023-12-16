package me.lagggpixel.core.modules.skills.handlers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.skills.SkillsModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SkillHandler {
  private final SkillsModule skillsModule;

  @Getter
  private final List<Location> nonNaturalBlocks = new ArrayList<>();

  @Getter
  private final Map<Material, Double> farmingBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> farmingEntities = new HashMap<>();
  @Getter
  private final Map<Material, Double> miningBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> miningEntities = new HashMap<>();
  @Getter
  private final Map<Material, Double> combatBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> combatEntities = new HashMap<>();

  public SkillHandler(SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
    File file = skillsModule.getSkill_exp();
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    //<editor-fold desc="Load Farming">
    ConfigurationSection farmingSection = yamlConfiguration.getConfigurationSection("farming");
    if (farmingSection != null) {
      ConfigurationSection farmingBlocksSection = farmingSection.getConfigurationSection("blocks");
      if (farmingBlocksSection != null) {
        farmingBlocksSection.getKeys(false).forEach((k) -> {
          double exp = farmingBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, farmingBlocks);
        });
      }

      ConfigurationSection farmingMobsSection = farmingSection.getConfigurationSection("mobs");
      if (farmingMobsSection != null) {
        farmingMobsSection.getKeys(false).forEach((k) -> {
          double exp = farmingMobsSection.getDouble(k);
          checkMobToAppend(k, exp, farmingEntities);
        });
      }
    }
    //</editor-fold>

    //<editor-fold desc="Load Mining">
    ConfigurationSection miningSection = yamlConfiguration.getConfigurationSection("mining");
    if (miningSection != null) {
      ConfigurationSection miningBlocksSection = miningSection.getConfigurationSection("blocks");
      if (miningBlocksSection != null) {
        miningBlocksSection.getKeys(false).forEach((k) -> {
          double exp = miningBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, miningBlocks);
        });
      }

      ConfigurationSection miningMobsSection = miningSection.getConfigurationSection("mobs");
      if (miningMobsSection != null) {
        miningMobsSection.getKeys(false).forEach((k) -> {
          double exp = miningMobsSection.getDouble(k);
          checkMobToAppend(k, exp, miningEntities);
        });
      }
    }
    //</editor-fold>

    //<editor-fold desc="Load Combat">
    ConfigurationSection combatSection = yamlConfiguration.getConfigurationSection("combat");
    if (combatSection != null) {
      ConfigurationSection combatBlocksSection = combatSection.getConfigurationSection("blocks");
      if (combatBlocksSection != null) {
        combatBlocksSection.getKeys(false).forEach((k) -> {
          double exp = combatBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, combatBlocks);
        });
      }

      ConfigurationSection combatMobsSection = combatSection.getConfigurationSection("combat");
      if (combatMobsSection != null) {
        combatMobsSection.getKeys(false).forEach((k) -> {
          double exp = combatMobsSection.getDouble(k);
          checkMobToAppend(k, exp, combatEntities);
        });
      }
    }
    //</editor-fold>
  }

  private void checkMaterialToAppend(String input, double exp, Map<Material, Double> list) {
    Material material;
    try {
      material = Material.valueOf(input);
    }
    catch (IllegalArgumentException exception) {
      Main.log(Level.WARNING, "skill_exp.yml is miss configured. The material " + input + " was not found.");
      return;
    }
    if (list.containsKey(material)) {
      Main.log(Level.WARNING, "skill_exp.yml is miss configured. The material" + input + " was found twice in one skill.");
      return;
    }
    list.put(material, exp);
  }

  private void checkMobToAppend(String input, double exp, Map<EntityType, Double> list) {
    EntityType entityType;
    try {
      entityType = EntityType.valueOf(input);
    } catch (IllegalArgumentException exception) {
      Main.log(Level.WARNING, "skill_exp.yml is miss configured. The entity type " + input + " was not found.");
      return;
    }
    if (list.containsKey(entityType)) {
      Main.log(Level.WARNING, "skill_exp.yml is miss configured. The entity type" + input + " was found twice in one skill.");
      return;
    }
    list.put(entityType, exp);
  }

  public boolean isBlockFarming(Block block) {
    return farmingBlocks.containsKey(block.getType());
  }

  public boolean isMobFarming(Entity entity) {
    return farmingEntities.containsKey(entity.getType());
  }

  public boolean isBlockMining(Block block) {
    return miningBlocks.containsKey(block.getType());
  }

  public boolean isMobMining(Entity entity) {
    return miningEntities.containsKey(entity.getType());
  }

  public boolean isBlockCombat(Block block) {
    return combatBlocks.containsKey(block.getType());
  }

  public boolean isMobCombat(Entity entity) {
    return combatEntities.containsKey(entity.getType());
  }
}
