/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.skills.handlers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.skills.SkillsModule;
import me.lagggpixel.core.utils.ExceptionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkillHandler {
  private final SkillsModule skillsModule;

  @Getter
  private static final Map<Integer, Long> skillExpPerLevel = new HashMap<>();
  @Getter
  private static final Map<Integer, Long> moneyPerLevel = new HashMap<>();

  @Getter
  private final List<Location> nonNaturalBlocks = new ArrayList<>();

  @Getter
  private final Map<Material, Double> farmingBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> farmingEntities = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> farmingBreedMobs = new HashMap<>();
  @Getter
  private final Map<Material, Double> farmingFish = new HashMap<>();

  @Getter
  private final Map<Material, Double> miningBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> miningEntities = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> miningBreedMobs = new HashMap<>();
  @Getter
  private final Map<Material, Double> miningFish = new HashMap<>();

  @Getter
  private final Map<Material, Double> combatBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> combatEntities = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> combatBreedMobs = new HashMap<>();
  @Getter
  private final Map<Material, Double> combatFish = new HashMap<>();

  @Getter
  private final Map<Material,Double> woodcuttingBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> woodcuttingEntities = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> woodcuttingBreedMobs = new HashMap<>();
  @Getter
  private final Map<Material, Double> woodcuttingFish = new HashMap<>();

  @Getter
  private final Map<Material, Double> fishingBlocks = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> fishingEntities = new HashMap<>();
  @Getter
  private final Map<EntityType, Double> fishingBreedMobs = new HashMap<>();
  @Getter
  private final Map<Material, Double> fishingFish = new HashMap<>();

  public SkillHandler(@NotNull SkillsModule skillsModule) {
    this.skillsModule = skillsModule;
    YamlConfiguration skillExpConfiguration = YamlConfiguration.loadConfiguration(skillsModule.getSkill_exp());
    //<editor-fold desc="Load Farming">
    ConfigurationSection farmingSection = skillExpConfiguration.getConfigurationSection("farming");
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

      ConfigurationSection farmingBreedMobsSection = farmingSection.getConfigurationSection("breed");
      if (farmingBreedMobsSection != null) {
        farmingBreedMobsSection.getKeys(false).forEach((k) -> {
          double exp = farmingBreedMobsSection.getDouble(k);
          checkMobToAppend(k, exp, farmingBreedMobs);
        });
      }

      ConfigurationSection farmingFishSection = farmingSection.getConfigurationSection("fish");
      if (farmingFishSection != null) {
        farmingFishSection.getKeys(false).forEach((k) -> {
          double exp = farmingFishSection.getDouble(k);
          checkMaterialToAppend(k, exp, farmingFish);
        });
      }
    }
    //</editor-fold>
    //<editor-fold desc="Load Mining">
    ConfigurationSection miningSection = skillExpConfiguration.getConfigurationSection("mining");
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

      ConfigurationSection miningBreedMobsSection = miningSection.getConfigurationSection("breed");
      if (miningBreedMobsSection != null) {
        miningBreedMobsSection.getKeys(false).forEach((k) -> {
          double exp = miningBreedMobsSection.getDouble(k);
          checkMobToAppend(k, exp, miningBreedMobs);
        });
      }

      ConfigurationSection miningFishSection = miningSection.getConfigurationSection("fish");
      if (miningFishSection != null) {
        miningFishSection.getKeys(false).forEach((k) -> {
          double exp = miningFishSection.getDouble(k);
          checkMaterialToAppend(k, exp, miningFish);
        });
      }
    }
    //</editor-fold>
    //<editor-fold desc="Load Combat">
    ConfigurationSection combatSection = skillExpConfiguration.getConfigurationSection("combat");
    if (combatSection != null) {
      ConfigurationSection combatBlocksSection = combatSection.getConfigurationSection("blocks");
      if (combatBlocksSection != null) {
        combatBlocksSection.getKeys(false).forEach((k) -> {
          double exp = combatBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, combatBlocks);
        });
      }

      ConfigurationSection combatMobsSection = combatSection.getConfigurationSection("mobs");
      if (combatMobsSection != null) {
        combatMobsSection.getKeys(false).forEach((k) -> {
          double exp = combatMobsSection.getDouble(k);
          checkMobToAppend(k, exp, combatEntities);
        });
      }

      ConfigurationSection combatBreedMobsSection = combatSection.getConfigurationSection("breed");
      if (combatBreedMobsSection != null) {
        combatBreedMobsSection.getKeys(false).forEach((k) -> {
          double exp = combatBreedMobsSection.getDouble(k);
          checkMobToAppend(k, exp, combatBreedMobs);
        });
      }

      ConfigurationSection combatFishSection = combatSection.getConfigurationSection("fish");
      if (combatFishSection != null) {
        combatFishSection.getKeys(false).forEach((k) -> {
          double exp = combatFishSection.getDouble(k);
          checkMaterialToAppend(k, exp, combatFish);
        });
      }
    }
    //</editor-fold>
    //<editor-fold desc="Load Woodcutting">
    ConfigurationSection woodcuttingSection = skillExpConfiguration.getConfigurationSection("woodcutting");
    if (woodcuttingSection != null) {
      ConfigurationSection woodcuttingBlocksSection = woodcuttingSection.getConfigurationSection("blocks");
      if (woodcuttingBlocksSection != null) {
        woodcuttingBlocksSection.getKeys(false).forEach((k) -> {
          double exp = woodcuttingBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, woodcuttingBlocks);
        });
      }

      ConfigurationSection woodcuttingMobsSection = woodcuttingSection.getConfigurationSection("mobs");
      if (woodcuttingMobsSection != null) {
        woodcuttingMobsSection.getKeys(false).forEach((k) -> {
          double exp = woodcuttingMobsSection.getDouble(k);
          checkMobToAppend(k, exp, woodcuttingEntities);
        });
      }

      ConfigurationSection woodcuttingBreedMobsSection = woodcuttingSection.getConfigurationSection("breed");
      if (woodcuttingBreedMobsSection != null) {
        woodcuttingBreedMobsSection.getKeys(false).forEach((k) -> {
          double exp = woodcuttingBreedMobsSection.getDouble(k);
          checkMobToAppend(k, exp, woodcuttingBreedMobs);
        });
      }

      ConfigurationSection woodcuttingFishSection = woodcuttingSection.getConfigurationSection("fish");
      if (woodcuttingFishSection != null) {
        woodcuttingFishSection.getKeys(false).forEach((k) -> {
          double exp = woodcuttingFishSection.getDouble(k);
          checkMaterialToAppend(k, exp, woodcuttingFish);
        });
      }
    }
    //</editor-fold>
    //<editor-fold desc="Load Fishing">
    ConfigurationSection fishingSection = skillExpConfiguration.getConfigurationSection("fishing");
    if (fishingSection != null) {

      ConfigurationSection fishingBlocksSection = fishingSection.getConfigurationSection("blocks");
      if (fishingBlocksSection != null) {
        fishingBlocksSection.getKeys(false).forEach((k) -> {
          double exp = fishingBlocksSection.getDouble(k);
          checkMaterialToAppend(k, exp, fishingBlocks);
        });
      }

      ConfigurationSection fishingMobsSection = fishingSection.getConfigurationSection("mobs");
      if (fishingMobsSection != null) {
        fishingMobsSection.getKeys(false).forEach((k) -> {
          double exp = fishingMobsSection.getDouble(k);
          checkMobToAppend(k, exp, fishingEntities);
        });
      }

      ConfigurationSection fishingBreedMobsSection = fishingSection.getConfigurationSection("breed");
      if (fishingBreedMobsSection != null) {
        fishingBreedMobsSection.getKeys(false).forEach((k) -> {
          double exp = fishingBreedMobsSection.getDouble(k);
          checkMobToAppend(k, exp, fishingBreedMobs);
        });
      }

      ConfigurationSection fishingFishSection = fishingSection.getConfigurationSection("fish");
      if (fishingFishSection != null) {
        fishingFishSection.getKeys(false).forEach((k) -> {
          double exp = fishingFishSection.getDouble(k);
          checkMaterialToAppend(k, exp, fishingFish);
        });
      }
    }
    //</editor-fold>

    YamlConfiguration skillLevelUpConfiguration = YamlConfiguration.loadConfiguration(skillsModule.getSkill_level_up());
    skillLevelUpConfiguration.getKeys(false).forEach((k) -> {
      try {
        int level = Integer.parseInt(k);
        ConfigurationSection section = skillLevelUpConfiguration.getConfigurationSection(k);
        if (section != null) {
          long exp = section.getLong("exp");
          long reward = section.getLong("reward");
          skillExpPerLevel.put(level, exp);
          moneyPerLevel.put(level, reward);
        }
      } catch (NumberFormatException ignored) {
      }
    });

    YamlConfiguration nonNaturalBlocksConfiguration = YamlConfiguration.loadConfiguration(skillsModule.getNon_natural_blocks());
    nonNaturalBlocksConfiguration.getKeys(false).forEach((k) -> {
      Location location  = nonNaturalBlocksConfiguration.getLocation(k);
      nonNaturalBlocks.add(location);
    });
  }

  public void saveNonNaturalBlocks() {
    YamlConfiguration config = new YamlConfiguration();
    for (Location location : nonNaturalBlocks) {
      config.set(String.valueOf(UUID.randomUUID()), location);
    }
    try {
      config.save(skillsModule.getNon_natural_blocks());
    } catch (IOException e) {
      ExceptionUtils.handleException(e);
    }
  }

  private void checkMaterialToAppend(String input, double exp, Map<Material, Double> list) {
    Material material;
    try {
      material = Material.valueOf(input);
    } catch (IllegalArgumentException exception) {
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

  public boolean isBlockFarming(@NotNull Block block) {
    return farmingBlocks.containsKey(block.getType());
  }

  public boolean isMobFarming(@NotNull Entity entity) {
    return farmingEntities.containsKey(entity.getType());
  }

  public boolean isMobBreedFarming(@NotNull Entity entity) {
    return farmingBreedMobs.containsKey(entity.getType());
  }

  public boolean isFishFarming(@NotNull Material material) {
    return farmingFish.containsKey(material);
  }

  public boolean isBlockMining(@NotNull Block block) {
    return miningBlocks.containsKey(block.getType());
  }

  public boolean isMobMining(@NotNull Entity entity) {
    return miningEntities.containsKey(entity.getType());
  }

  public boolean isMobBreedMining(@NotNull Entity entity) {
    return miningBreedMobs.containsKey(entity.getType());
  }

  public boolean isFishMining(@NotNull Material material) {
    return miningFish.containsKey(material);
  }

  public boolean isBlockCombat(@NotNull Block block) {
    return combatBlocks.containsKey(block.getType());
  }

  public boolean isMobCombat(@NotNull Entity entity) {
    return combatEntities.containsKey(entity.getType());
  }

  public boolean isMobBreedCombat(@NotNull Entity entity) {
    return combatBreedMobs.containsKey(entity.getType());
  }

  public boolean isFishCombat(@NotNull Material material) {
    return combatFish.containsKey(material);
  }

  public boolean isBlockWoodcutting(@NotNull Block block) {
    return woodcuttingBlocks.containsKey(block.getType());
  }

  public boolean isMobWoodcutting(@NotNull Entity entity) {
    return woodcuttingEntities.containsKey(entity.getType());
  }

  public boolean isMobBreedWoodcutting(@NotNull Entity entity) {
    return woodcuttingBreedMobs.containsKey(entity.getType());
  }

  public boolean isFishWoodcutting(@NotNull Material material) {
    return woodcuttingFish.containsKey(material);
  }

  public boolean isBlockFishing(@NotNull Block block) {
    return fishingBlocks.containsKey(block.getType());
  }

  public boolean isMobFishing(@NotNull Entity entity) {
    return fishingEntities.containsKey(entity.getType());
  }

  public boolean isMobBreedFishing(@NotNull Entity entity) {
    return fishingBreedMobs.containsKey(entity.getType());
  }
  public boolean isFishFishing(@NotNull Material material) {
    return fishingFish.containsKey(material);
  }
}
