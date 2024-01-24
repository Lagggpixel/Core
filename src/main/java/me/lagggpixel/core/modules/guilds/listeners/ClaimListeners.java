/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.guilds.listeners;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.ClaimProfile;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.data.Pillar;
import me.lagggpixel.core.modules.guilds.handlers.ClaimManager;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.modules.guilds.handlers.PillarManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


@SuppressWarnings("FieldCanBeLocal")
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class ClaimListeners implements Listener {
  
  private final int CLAIM_PRICE_MULTIPLER = 100;
  private final int CLAIM_MINIMUM = 5;
  private final List<String> worlds = List.of("world", "world_nether");
  private final String ADMIN_NODE_CLAIM_INTERACTION_BYPASS = "core.guilds.claims.admin_interaction_bypass";
  private final String ADMIN_NODE_CLAIM_WORLD_BYPASS = "core.guilds.claims.admin_world_bypass";
  private final String ADMIN_NODE_CLAIM_MONEY_BYPASS = "core.guilds.claims.admin_money_bypass";
  private final String ADMIN_NODE_CLAIM_TOO_FAR_BYPASS = "core.guilds.claims.admin_too_far_bypass";
  private final String ADMIN_NODE_CLAIM_TOO_CLOSE_BYPASS = "core.guilds.claims.admin_too_close_bypass";
  private final boolean EXPLOSION_PROTECTION_ENABLED = true; // Set to false if not implemented
  private final String ADMIN_NODE_EXPLOSION_BYPASS = "core.guilds.explosion_bypass";
  
  private final GuildModule guildModule = GuildModule.getInstance();
  private final ClaimManager claimManager = guildModule.getClaimManager();
  private final GuildHandler guildHandler = this.guildModule.getGuildHandler();
  private final HashSet<ClaimProfile> profiles = new HashSet<>();
  private final ArrayList<UUID> clicked = new ArrayList<>();
  private final PillarManager pillarManager = guildModule.getPillarManager();

  public ClaimListeners() {
      this.guildModule = GuildModule.getInstance();
      this.claimManager = guildModule.getClaimManager();
      this.guildHandler = guildModule.getGuildHandler();
      this.profiles = new HashSet<>();
      this.clicked = new ArrayList<>();
      this.pillarManager = guildModule.getPillarManager();

      Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }

  private ClaimProfile getProfile(UUID id) {
    for (ClaimProfile profile : this.profiles) {
      if (profile.getUuid() == id) {
        return profile;
      }
    }
    ClaimProfile newProfile = new ClaimProfile(id);
    this.profiles.add(newProfile);
    return newProfile;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClaimInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    User user = Main.getUser(player);

    if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null && clickedBlock.getType() == Material.PISTON) {
        // Handle piston interaction within claims
           handlePistonInteraction(event, clickedBlock);
      }
    }

    if (EXPLOSION_PROTECTION_ENABLED && event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.isCancelled()) {
      if (isExplosiveItem(event.getClickedBlock(), player)) {
        event.setCancelled(true);
        user.sendMessage("Explosion protection message"); // Replace with your message
        return;
      }
    }
    if (event.getAction() == Action.PHYSICAL) {
      for (Claim claim : this.claimManager.getClaims()) {
        if (event.getClickedBlock() == null) {
          return;
        }
        if (claim.isInside(event.getClickedBlock().getLocation(), false)) {
          Guild playerFaction = this.guildHandler.getGuildFromPlayer(player);
          if (handleClaimInteraction(event, claim, playerFaction)) {
            return;
          }
        }
      }
    } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (event.getClickedBlock() == null) {
        return;
      }
      if ((player.getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET
          || player.getInventory().getItemInMainHand().getType() == Material.LAVA_BUCKET
          || player.getInventory().getItemInMainHand().getType() == Material.FLINT_AND_STEEL)
          && (isInteractiveBlock(event.getClickedBlock()))) {
        for (Claim claim : this.claimManager.getClaims()) {
          if (claim.isInside(event.getClickedBlock().getLocation(), false)) {
            Guild playerFaction = guildHandler.getGuildFromPlayer(player);
            if (handleClaimInteraction(event, claim, playerFaction)) return;
            user.sendMessage(Lang.GUILD_CLAIM_NO_INTERACT.toComponentWithPrefix());
          }
        }
      }
      
      if (isInteractiveBlock(event.getClickedBlock())) {
        for (Claim claim : this.claimManager.getClaims()) {
          if (claim.isInside(event.getClickedBlock().getLocation(), false)) {
            Guild guild = guildHandler.getGuildFromPlayer(player);
            if (handleClaimInteraction(event, claim, guild)) return;
            user.sendMessage(Lang.GUILD_CLAIM_NO_INTERACT.toComponentWithPrefix());
          }
        }
      }
    }
  }
  
  /**
   * Handles the claim interaction for a player.
   *
   * @param e     the PlayerInteractEvent
   * @param claim the Claim object
   * @param guild the PlayerFaction object
   * @return true if the claim interaction is handled successfully, false otherwise
   */
  private boolean isExplosiveItem(Block block, Player player) {
    Material itemType = player.getInventory().getItemInMainHand().getType();
    return (itemType == Material.WATER_BUCKET || itemType == Material.LAVA_BUCKET || itemType == Material.FLINT_AND_STEEL)
            && isInteractiveBlock(block);
  }

  private boolean handleExplosionBypass(Player player) {
  return player.hasPermission(ADMIN_NODE_EXPLOSION_BYPASS);
  }
  private boolean handleClaimInteraction(PlayerInteractEvent e, Claim claim, Guild guild) {
    if ((guild != null && guild == claim.getOwner())
        || e.getPlayer().hasPermission(ADMIN_NODE_CLAIM_INTERACTION_BYPASS)) {
      return true;
    }
    e.setCancelled(true);
    return false;
  }
  
  
  @EventHandler(priority = EventPriority.LOW)
  public void onClaimInteract(BlockBreakEvent e) {
    handleClaimInteract(e);

    // Check if a mob is breaking a block within a claim
    if (isMobBreakingBlock(e)) {
      handleMobBlockBreak(e);
    }
  }

// Additional method to check if a mob is breaking a block
private boolean isMobBreakingBlock(BlockBreakEvent event) {
  return event.getPlayer() instanceof Monster; // Customize this condition based on your mob type checks
}

// Additional method to handle mob block break prevention
private void handleMobBlockBreak(BlockBreakEvent event) {
  Player player = event.getPlayer();
  for (Claim claim : this.claimManager.getClaims()) {
    if (claim.isInside(event.getBlock().getLocation(), false)) {
      Guild playerFaction = guildHandler.getGuildFromPlayer(player);
      if ((playerFaction != null && playerFaction == claim.getOwner()) || player.hasPermission(ADMIN_NODE_CLAIM_INTERACTION_BYPASS)) {
        return; // Allow mob block break for claim owner or admin
      }
      event.setCancelled(true);
      Main.getUser(player).sendMessage("Mob block destruction prevention message"); // Replace with your message
    }
  }
}
  @EventHandler(priority = EventPriority.LOW)
  public void onClaimInteract(BlockPlaceEvent e) {
    handleClaimInteract(e);
  }
  
  /**
   * Sends a claim change notification to the player.
   *
   * @param p        the player to send the notification to
   * @param guild    the guild involved in the claim change
   * @param entering true if the player is entering the claim, false if leaving
   */
  private void handlePistonInteraction(PlayerInteractEvent event, Block pistonBlock) {
    Player player = event.getPlayer();
    User user = Main.getUser(player);

    for (Claim claim : this.claimManager.getClaims()) {
      if (claim.isInside(pistonBlock.getLocation(), false)) {
        Guild playerFaction = guildHandler.getGuildFromPlayer(player);
        if ((playerFaction != null && playerFaction == claim.getOwner()) || player.hasPermission(ADMIN_NODE_CLAIM_INTERACTION_BYPASS)) {
          return; // Allow piston interaction for claim owner or admin
        }

        event.setCancelled(true);
        user.sendMessage("Piston protection message"); // Replace with your message
      }
    }
  }
  private void sendClaimChange(Player p, Guild guild, boolean entering) {
    Guild playerFaction = guildHandler.getGuildFromPlayer(p);
    if (entering) {
      if (playerFaction == null) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_ENTERING_ENEMY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
        return;
      }
      if (guild == playerFaction) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_ENTERING_FRIENDLY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      } else if (guild.getAllies().contains(playerFaction) || playerFaction.getAllies().contains(guild)) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_ENTERING_ALLY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      } else {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_ENTERING_ENEMY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      }
    } else {
      if (playerFaction == null) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_LEAVING_ENEMY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
        return;
      }
      if (guild == playerFaction) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_LEAVING_FRIENDLY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      } else if (guild.getAllies().contains(playerFaction) || playerFaction.getAllies().contains(guild)) {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_LEAVING_ALLY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      } else {
        p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_LEAVING_ENEMY.toComponentWithPrefix(Map.of("%guild%", guild.getName())));
      }
    }
  }
  
  @EventHandler
  public void onMove(PlayerMoveEvent e) {
    if (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ()) {
      final Player p = e.getPlayer();
      final ClaimProfile profile = getProfile(p.getUniqueId());
      for (Claim claim : this.claimManager.getClaims()) {
        if (claim.isInside(e.getTo(), true) && claim.getWorld() == p.getWorld()) {
          if (profile.getLastInside() == null) {
            profile.setLastInside(claim);
            p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_LEAVING_SYSTEM.toComponentWithPrefix());
            sendClaimChange(p, claim.getOwner(), true);
            return;
          }
          if (profile.getLastInside().getOwner() != claim.getOwner()) {
            sendClaimChange(p, profile.getLastInside().getOwner(), false);
            sendClaimChange(p, claim.getOwner(), true);
          }
          
          profile.setLastInside(claim);
          continue;
        }
        if (profile.getLastInside() != null && profile.getLastInside() == claim) {
          (new BukkitRunnable() {
            public void run() {
              if (profile.getLastInside() != null && profile.getLastInside() == claim) {
                ClaimListeners.this.sendClaimChange(p, claim.getOwner(), false);
                p.sendMessage(Lang.GUILD_CLAIM_MESSAGES_ENTERING_SYSTEM.toComponentWithPrefix());
                profile.setLastInside(null);
              }
            }
          }).runTaskLater(Main.getInstance(), 1L);
        }
      }
    }
  }
  
  
  @EventHandler
  public void onDrop(PlayerDropItemEvent e) {
    if (this.claimManager.isWand(e.getItemDrop().getItemStack())) {
      e.getItemDrop().remove();
    }
  }
  
  @EventHandler
  public void onStore(InventoryMoveItemEvent e) {
    if (this.claimManager.isWand(e.getItem())) {
      e.getSource().remove(e.getItem());
      e.getDestination().remove(e.getItem());
    }
  }
  
  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    if (this.claimManager.isWand(e.getItem())) {
      Player p = e.getPlayer();
      final ClaimProfile prof = getProfile(p.getUniqueId());
      Guild guild = null;
      if (claimManager.getClaiming().containsKey(p.getUniqueId())) {
        guild = claimManager.getClaiming().get(p.getUniqueId());
      }
      if (guild == null) {
        guild = guildHandler.getGuildFromPlayer(p);
      }
      if (guild == null) {
        return;
      }
      if (e.getAction() == Action.RIGHT_CLICK_AIR) {
        e.setCancelled(true);
        if (prof.getX1() != 0 && prof.getX2() != 0 && prof.getZ1() != 0 && prof.getZ2() != 0) {
          if (!this.clicked.contains(p.getUniqueId())) {
            p.sendMessage(Lang.GUILD_WAND_MESSAGES_CLEAR.toComponentWithPrefix());
            this.clicked.add(p.getUniqueId());
          } else {
            Pillar two = this.pillarManager.getPillar(prof, "second");
            Pillar one = this.pillarManager.getPillar(prof, "first");
            if (one != null) {
              one.removePillar();
            }
            if (two != null) {
              two.removePillar();
            }
            p.sendMessage(Lang.GUILD_WAND_MESSAGES_CLEARED.toComponentWithPrefix());
            this.clicked.remove(p.getUniqueId());
            prof.setX1(0);
            prof.setZ1(0);
            prof.setZ2(0);
            prof.setX2(0);
            
            return;
          }
        } else {
          p.sendMessage(Lang.GUILD_WAND_MESSAGES_INVALID_SELECTION.toComponentWithPrefix());
          
          return;
        }
      }
      // Claim land with wand
      if (e.getAction() == Action.LEFT_CLICK_AIR && p.isSneaking()) {
        if (prof.getX1() != 0 && prof.getX2() != 0 && prof.getZ1() != 0 && prof.getZ2() != 0) {
          for (Claim claim1 : this.claimManager.getClaims()) {
            if (claim1.overlaps(prof.getX1(), prof.getZ1(), prof.getX2(), prof.getZ2()) && claim1.getWorld() == p.getWorld()) {
              p.sendMessage(Lang.GUILD_WAND_MESSAGES_OVERCLAIM.toComponentWithPrefix());
              
              return;
            }
          }
          if (!this.worlds.contains(p.getWorld().getName()) && !p.hasPermission(ADMIN_NODE_CLAIM_WORLD_BYPASS)) {
            p.sendMessage(Lang.GUILD_WAND_MESSAGES_OTHER.toComponentWithPrefix());
            return;
          }
          
          Pillar two = this.pillarManager.getPillar(prof, "second");
          Pillar one = this.pillarManager.getPillar(prof, "first");
          
          Location loc1 = new Location(p.getWorld(), prof.getX1(), 0.0D, prof.getZ1());
          Location loc2 = new Location(p.getWorld(), prof.getX2(), 0.0D, prof.getZ2());
          
          int price = (int) Math.round(loc1.distance(loc2) * CLAIM_PRICE_MULTIPLER);
          
          
          if (price > guild.getBalance() && !p.hasPermission(ADMIN_NODE_CLAIM_MONEY_BYPASS)) {
            p.sendMessage(Lang.GUILD_WAND_MESSAGES_INVALID_FUNDS.toComponentWithPrefix());
            return;
          }
          guild.setBalance(guild.getBalance() - price);
          guild.sendMessage(Lang.GUILD_WAND_MESSAGES_CLEAR.toComponentWithPrefix(Map.of("%player%", p.getName())));
          
          if (one != null) {
            one.removePillar();
          }
          if (two != null) {
            two.removePillar();
          }
          
          
          claimManager.getClaiming().remove(p.getUniqueId());
          
          Claim claim = new Claim(UUID.randomUUID().toString() + UUID.randomUUID(), guild, prof.getX1(), prof.getX2(), prof.getZ1(), prof.getZ2(), p.getWorld(), price);
          this.claimManager.getClaims().add(claim);
          guild.getClaims().add(claim);
          prof.setX1(0);
          prof.setZ1(0);
          prof.setZ2(0);
          prof.setX2(0);
          p.getInventory().remove(p.getInventory().getItemInMainHand());
          
          return;
        }
        p.sendMessage(Lang.GUILD_WAND_MESSAGES_INVALID_SELECTION.toComponentWithPrefix());
        
        return;
      }
      
      // Set position 1
      if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
        e.setCancelled(true);
        
        if (e.getClickedBlock() == null) {
          return;
        }
        
        if (!guild.getClaims().isEmpty() && !guild.isNearBorder(e.getClickedBlock().getLocation()) && !p.hasPermission(ADMIN_NODE_CLAIM_TOO_FAR_BYPASS)) {
          p.sendMessage(Lang.GUILD_WAND_MESSAGES_TOO_FAR.toComponentWithPrefix());
          return;
        }
        if (checkIfPlayerCanClaim(e, p, guild)) {
          return;
        }
        prof.setX1(e.getClickedBlock().getX());
        prof.setZ1(e.getClickedBlock().getZ());
        
        handlePointSelection(p, guild, e.getClickedBlock(), prof, 1);
        
        Pillar pillar = this.pillarManager.getPillar(prof, "first");
        if (pillar != null) {
          this.pillarManager.getPillars().remove(pillar);
          pillar.removePillar();
        }
        pillar = new Pillar(prof, Material.DIAMOND_BLOCK, (byte) 0, e.getClickedBlock().getLocation(), "first");
        pillar.sendPillar();
      }
      
      // Set position 2
      if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
        e.setCancelled(true);
        
        if (e.getClickedBlock() == null) {
          return;
        }
        if (checkIfPlayerCanClaim(e, p, guild)) {
          return;
        }
        prof.setX2(e.getClickedBlock().getX());
        prof.setZ2(e.getClickedBlock().getZ());
        
        handlePointSelection(p, guild, e.getClickedBlock(), prof, 2);
        
        Pillar pillar = this.pillarManager.getPillar(prof, "second");
        if (pillar != null) {
          this.pillarManager.getPillars().remove(pillar);
          pillar.removePillar();
        }
        Pillar pillar1 = new Pillar(prof, Material.DIAMOND_BLOCK, (byte) 0, e.getClickedBlock().getLocation(), "second");
        (new BukkitRunnable() {
          public void run() {
            Pillar pillar = ClaimListeners.this.pillarManager.getPillar(prof, "second");
            pillar.sendPillar();
          }
        }).runTaskLater(Main.getInstance(), 1L);
      }
      
    }
  }
  
  /**
   * Calculates the price for claiming a guild territory based on the distance between two locations.
   *
   * @param p     the player who wants to claim the territory
   * @param guild the guild that the territory belongs to
   * @param loc1  the first location
   * @param loc2  the second location
   */
  private void checkIsClaimAffordable(Player p, Guild guild, Location loc1, Location loc2) {
    int price = (int) Math.round(loc1.distance(loc2) * CLAIM_PRICE_MULTIPLER);
    
    if (guild.getBalance() < price) {
      p.sendMessage(Lang.GUILD_WAND_MESSAGES_COST_TOO_MUCH.toComponentWithPrefix(Map.of("%amount%", price + "")));
    } else {
      p.sendMessage(Lang.GUILD_WAND_MESSAGES_COST_ENOUGH.toComponentWithPrefix(Map.of("%amount%", price + "")));
    }
  }
  
  /**
   * Checks if a player can claim a specific block.
   *
   * @param e     the PlayerInteractEvent triggered by the player
   * @param p     the player who wants to claim the block
   * @param guild the guild the player belongs to
   * @return true   if the player can claim the block, false otherwise
   */
  private boolean checkIfPlayerCanClaim(PlayerInteractEvent e, Player p, Guild guild) {
    if (e.getClickedBlock() == null) {
      return true;
    }
    for (Claim claim : this.claimManager.getClaims()) {
      if (claim.getWorld() == e.getClickedBlock().getLocation().getWorld()) {
        if (claim.isInside(e.getClickedBlock().getLocation(), false)) {
          p.sendMessage(Lang.GUILD_WAND_MESSAGES_OTHER.toComponentWithPrefix());
          return true;
        }
        Location blockLoc = e.getClickedBlock().getLocation();
        blockLoc.setY(claim.getCornerFour().getY());
        if (claim.isNearby(blockLoc) && claim.getOwner() != guild && !p.hasPermission(ADMIN_NODE_CLAIM_TOO_CLOSE_BYPASS)) {
          p.sendMessage(Lang.GUILD_WAND_MESSAGES_TOO_CLOSE.toComponentWithPrefix());
          return true;
        }
      }
    }
    if (!this.worlds.contains(p.getWorld().getName()) && !p.hasPermission(ADMIN_NODE_CLAIM_WORLD_BYPASS)) {
      p.sendMessage(Lang.GUILD_WAND_MESSAGES_OTHER.toComponentWithPrefix());
      
      return true;
    }
    return false;
  }
  
  /**
   * Determines if a given block is an interactive block.
   *
   * @param block the block to check
   * @return true if the block is an interactive block, false otherwise
   */
  private boolean isInteractiveBlock(Block block) {
    return block.getState() instanceof org.bukkit.inventory.InventoryHolder || block instanceof org.bukkit.inventory.InventoryHolder
        || block.getType().name().contains("CHEST")
        || block.getType().name().contains("FURNACE")
        || block.getType().name().contains("GATE")
        || block.getType().name().contains("DOOR")
        || block.getType().name().contains("BUTTON")
        || block.getType().name().contains("LEVER");
  }
  
  private void handlePointSelection(Player p, Guild guild, Block clickedBlock, ClaimProfile prof, int claimNumber) {
    if (prof.getX2() != 0 && prof.getZ2() != 0) {
      Location loc1 = new Location(p.getWorld(), prof.getX1(), 0.0D, prof.getZ1());
      Location loc2 = new Location(p.getWorld(), prof.getX2(), 0.0D, prof.getZ2());
      
      if (loc1.distance(loc2) < CLAIM_MINIMUM) {
        p.sendMessage(Lang.GUILD_WAND_MESSAGES_TOO_SMALL.toComponentWithPrefix());
        return;
      }
      
      handleClaimMessage(p, clickedBlock, claimNumber);
      
      checkIsClaimAffordable(p, guild, loc1, loc2);
    } else {
      handleClaimMessage(p, clickedBlock, claimNumber);
    }
  }
  
  private void handleClaimMessage(Player p, Block clickedBlock, int claimNumber) {
    if (claimNumber == 1) {
      p.sendMessage(Lang.GUILD_WAND_MESSAGES_FIRST_POINT.toComponentWithPrefix(Map.of("%x%", clickedBlock.getX() + "", "%z%", clickedBlock.getZ() + "")));
    } else if (claimNumber == 2) {
      p.sendMessage(Lang.GUILD_WAND_MESSAGES_SECOND_POINT.toComponentWithPrefix(Map.of("%x%", clickedBlock.getX() + "", "%z%", clickedBlock.getZ() + "")));
    }
  }
  
  private void handleClaimInteract(BlockBreakEvent blockBreakEvent) {
    Player player = blockBreakEvent.getPlayer();
    User user = Main.getUser(player);
    for (Claim claim : this.claimManager.getClaims()) {
      if (claim.isInside(blockBreakEvent.getBlock().getLocation(), false)) {
        Guild playerFaction = guildHandler.getGuildFromPlayer(player);
        if ((playerFaction != null && playerFaction == claim.getOwner()) || player.hasPermission(ADMIN_NODE_CLAIM_INTERACTION_BYPASS)) {
          return;
        }
        blockBreakEvent.setCancelled(true);
        user.sendMessage(Lang.GUILD_CLAIM_NO_INTERACT.toComponentWithPrefix());
      }
    }
  }
  
  private void handleClaimInteract(BlockPlaceEvent blockPlaceEvent) {
    Player player = blockPlaceEvent.getPlayer();
    User user = Main.getUser(player);
    for (Claim claim : this.claimManager.getClaims()) {
      if (claim.isInside(blockPlaceEvent.getBlock().getLocation(), false)) {
        Guild guild = guildHandler.getGuildFromPlayer(player);
        if ((guild != null && guild == claim.getOwner()) || player.hasPermission(ADMIN_NODE_CLAIM_INTERACTION_BYPASS)) {
          return;
        }
        blockPlaceEvent.setCancelled(true);
        user.sendMessage(Lang.GUILD_CLAIM_NO_INTERACT.toComponentWithPrefix());
      }
    }
  }
}

