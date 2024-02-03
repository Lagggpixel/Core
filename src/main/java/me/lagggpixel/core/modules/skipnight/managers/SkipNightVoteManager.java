/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.modules.skipnight.managers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.user.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.skipnight.SkipNightModule;
import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SkipNightVoteManager implements Runnable, Listener {

  private final SkipNightModule module;

  private Timer timer;
  private SkipNightVoteType skipNightVoteType;
  private int yes, no, playerCount, countDown, afkCount;
  private BossBar bar;

  private List<SkipNightVoter> skipNightVoters;
  private List<SkipNightVoter> afkSkipNightVoters;
  private Player player;
  private User user;
  private World world;
  private SkipNightManager skipNightManager;

  public SkipNightVoteManager(SkipNightModule module) {
    timer = Timer.OFF;
    this.module = module;
  }

  @EventHandler
  public void onLogoff(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    if (timer != Timer.OFF && skipNightVoteType != null) {// vote is running
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());
      if (skipNightVoters.contains(skipNightVoter)) { // player is in voter list
        skipNightVoter = skipNightVoters.get(skipNightVoters.lastIndexOf(skipNightVoter));
        if (skipNightVoter.getVote() == 1) yes--;
        if (skipNightVoter.getVote() == -1) no--;
        skipNightVoters.remove(skipNightVoter);
      }

    }
  }

  @EventHandler
  public void onBedEnter(PlayerBedEnterEvent event) {
    Player player = event.getPlayer();
    User user = Main.getUser(player);

    if (timer != Timer.OFF && skipNightVoteType == SkipNightVoteType.NIGHT) { // vote is running at night
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());
      if (skipNightVoters.contains(skipNightVoter)) { // Voter exists but hasn't voted
        skipNightVoter = skipNightVoters.get(skipNightVoters.indexOf(skipNightVoter));
        if (skipNightVoter.getVote() == 0) {
          skipNightVoter.voteYes();
          yes++;

          user.sendMessage(Lang.SN_IN_BED_VOTED_YES.toComponentWithPrefix());
        }
      } else { // Voter doesn't exist but hasn't voted
        skipNightVoters.add(skipNightVoter);
        skipNightVoter.voteYes();
        yes++;
        user.sendMessage(Lang.SN_IN_BED_VOTED_YES.toComponentWithPrefix());
      }
    } else {
      if (player.getWorld().getPlayers().size() > 1) { // if player isn't only one in the world
        user.sendMessage(Lang.SN_IN_BED_NO_VOTE_IN_PROGRESS.toComponentWithPrefix());
      }
    }
  }


  public void run() {
    switch (timer) {
      case INIT:
        doInit();
        break;
      case OPERATION:
        doOperation();
        break;
      case INTERRUPT:
        doInterrupt();
        break;
      case CANCEL:
        doCancel();
        break;
      case FINAL:
        doFinal();
        break;
      case COMPLETE:
        doComplete();
        break;
      // case COOLDOWN:
      //     doCooldown();
      //     break;
      default:
        break;
    }
  }

  private void doInit() {
    skipNightVoters = new ArrayList<>();
    afkSkipNightVoters = new ArrayList<>();

    bar = BossBar.bossBar(Component.text(), 1.0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

    yes = 1;
    no = 0;
    countDown = 30;
    afkCount = 0;

    bar.name(currentVotePA(yes, no, afkCount)).color(BossBar.Color.PURPLE);

    updateAll(skipNightVoters, player);

    timer = Timer.OPERATION;
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
  }

  private void doOperation() {
    countDown--;
    if (yes + no == playerCount) timer = Timer.INTERRUPT;
    if (voteCancel()) timer = Timer.CANCEL;
    bar.progress((float) countDown / 30.0f);
    bar.name(currentVotePA(yes, no, afkCount));
    updateAll(skipNightVoters);
    if (countDown == 10) timer = Timer.FINAL;
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
  }

  private void doInterrupt() {
    countDown = 0;
    bar.progress(1.0f);
    bar.name(Lang.SN_ALL_PLAYERS_VOTED_BOSS_BAR.toComponent());
    bar.color(BossBar.Color.YELLOW);

    timer = Timer.COMPLETE;
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
  }

  private void doFinal() {
    countDown--;
    if (yes + no == playerCount) timer = Timer.INTERRUPT;
    if (voteCancel()) timer = Timer.CANCEL;
    bar.progress((float) countDown / 30.0f);
    bar.name(currentVotePA(yes, no, afkCount));
    if (countDown == 9)
      skipNightVoters = updateAll(skipNightVoters, Lang.SN_TEN_SECOND_LEFT.toComponentWithPrefix());
    else updateAll(skipNightVoters);

    if (countDown % 2 == 1) bar.color(BossBar.Color.WHITE);
    else bar.color(BossBar.Color.PURPLE);

    if (countDown == 0) timer = Timer.COMPLETE;
    Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
  }

  private void doComplete() {
    countDown--;
    if (countDown == -1) {
      bar.progress(1.0f);
      if (yes > no) {
        bar.name(Lang.SN_VOTE_PASSED_BOSS_BAR.toComponent());
        bar.color(BossBar.Color.GREEN);
        updateAll(skipNightVoters, Lang.SN_VOTE_PASSED_BOSS_BAR.toComponentWithPrefix());
        skipNightManager = new SkipNightManager(world, Main.getInstance(), skipNightVoteType);
        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), skipNightManager, 10);

        // Set boss bar progress to fast-forward progress
        bar.progress(0.0f);
        new BukkitRunnable() {

          @Override
          public void run() {
            float time = (float) world.getTime();
            if (time > 12000.0f) time -= 12000.0f;
            bar.progress(time / 12000.0f);
            if (countDown <= -8) {
              bar.progress(1.0f);
              this.cancel();
            }
          }
        }.runTaskTimer(Main.getInstance(), 0, 1);

        if (world.hasStorm()) world.setStorm(false);
      } else {
        bar.name(Lang.SN_VOTE_FAILED_BOSS_BAR.toComponent());
        bar.color(BossBar.Color.RED);
        updateAll(skipNightVoters, Lang.SN_VOTE_FAILED_BOSS_BAR.toComponentWithPrefix());
      }
      Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
    }

    if (countDown <= -2) Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);

    if (countDown == -9) {
      Main.getInstance().getServer().getOnlinePlayers().forEach(p -> p.hideBossBar(bar));
      bar = null;
      skipNightVoters = null;
      skipNightManager = null;
      skipNightVoteType = null;
      timer = yes > no ? Timer.OFF : Timer.COOLDOWN;
    }
  }

    /*
    private void doCooldown() {
        if (countDown >= (config.getCooldown() * -1) - 9)
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);
        else timer = Timer.OFF;
    }
    */

  private void doCancel() {
    if (countDown > 0) countDown = 0;
    if (countDown == 0) {
      bar.progress(1.0f);
      bar.color(BossBar.Color.BLUE);
      bar.name(Lang.SN_ALREADY_DAY_BOSS_BAR.toComponent());
    }

    countDown--;

    if (countDown > -4) Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), this, 20);

    if (countDown == -4) {
      Main.getInstance().getServer().getOnlinePlayers().forEach(p -> p.hideBossBar(bar));
      bar = null;
      skipNightVoters = null;
      skipNightManager = null;
      skipNightVoteType = null;
      timer = Timer.OFF;
    }
  }

  public void addYes(UUID uuid, SkipNightVoteType skipNightVoteType) {

    if (timer != Timer.OFF) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(uuid);
      if (!skipNightVoters.isEmpty() && skipNightVoters.contains(skipNightVoter)) {
        skipNightVoter = skipNightVoters.get(skipNightVoters.lastIndexOf(skipNightVoter));
        if (skipNightVoter.getVote() == 0) {
          yes++;
          skipNightVoter.voteYes();
          user.sendMessage(Lang.SN_VOTED_SELF.toComponentWithPrefix(Map.of("%status%", "yes")));
          // actionBarMessage(messages.playerHasVotedYes(Bukkit.getPlayer(uuid).getName()));
        } else user.sendMessage(Lang.SN_ALREADY_VOTED.toComponentWithPrefix());
      }
    } else user.sendMessage(noVoteInProgress());
  }

  public void addNo(UUID uuid, SkipNightVoteType skipNightVoteType) {
    if (timer != Timer.OFF) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(uuid);
      if (!skipNightVoters.isEmpty() && skipNightVoters.contains(skipNightVoter)) {
        skipNightVoter = skipNightVoters.get(skipNightVoters.lastIndexOf(skipNightVoter));
        if (skipNightVoter.getVote() == 0) {
          no++;
          skipNightVoter.voteNo();
          user.sendMessage(Lang.SN_VOTED_SELF.toComponentWithPrefix(Map.of("%status%", "no")));
          // actionBarMessage(messages.playerHasVotedNo(Bukkit.getPlayer(uuid).getName()));
        } else user.sendMessage(Lang.SN_ALREADY_VOTED.toComponentWithPrefix());
      }
    } else user.sendMessage(noVoteInProgress());
  }

  // Attempts to start a vote if all conditions are met, otherwise informs player why vote can't start
  public void start(Player player, SkipNightVoteType skipNightVoteType) {
    User user = Main.getUser(player);
    boolean isAfk = Main.getUser(player.getUniqueId()).isAfk();

    if (!isInOverworld(player)) // If player isn't in the overworld
      user.sendMessage(Lang.SN_WORLD_NO_OVERWORLD.toComponentWithPrefix());
    else if (skipNightVoteType == SkipNightVoteType.NIGHT && player.getWorld().getTime() < 12516) // If it's day, trying to skip night
      user.sendMessage(Lang.SN_CAN_ONLY_VOTE_AT_NIGHT.toComponentWithPrefix());
    else if (isAfk)
      user.sendMessage(Lang.SN_NO_VOTE_WHILE_AFK.toComponentWithPrefix());
      // else if (timer == Timer.COOLDOWN) // If the vote is in cooldown
      //    platform.player(player).sendMessage(messages.cooldown());
    else if (!(timer == Timer.OFF)) // If there's a vote happening
      user.sendMessage(Lang.SN_VOTE_ALREADY_IN_PROGRESS.toComponentWithPrefix());
    else {
      timer = Timer.INIT;
      this.skipNightVoteType = skipNightVoteType;
      this.player = player;
      this.user = user;
      world = player.getWorld();
      run();
    }
  }

  // Checks whether player is in overworld
  private boolean isInOverworld(Player player) {
    return player.getWorld().getEnvironment() == World.Environment.NORMAL;
  }

  public String voteTypeString() {
    return voteTypeString(this.skipNightVoteType);
  }

  public String voteTypeString(SkipNightVoteType skipNightVoteType) {
    return "night";
  }

  private void updateAll(List<SkipNightVoter> skipNightVoters) {
    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());
      User user = Main.getUser(player);
      boolean isAfk = Main.getUser(player.getUniqueId()).isAfk();

      if (isInOverworld(player)) {
        if (skipNightVoters.contains(skipNightVoter)) {
          skipNightVoter = skipNightVoters.get(skipNightVoters.indexOf(skipNightVoter));
          if (isAfk) { // in Active, change to Afk
            skipNightVoters.remove(skipNightVoter);
            afkSkipNightVoters.add(skipNightVoter);
            int vote = skipNightVoter.resetVote();
            if (vote == 1) yes--;
            if (vote == -1) no--;
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          }
        } else if (afkSkipNightVoters.contains(skipNightVoter)) {
          if (!isAfk) { // in Afk, change to Active
            afkSkipNightVoters.remove(skipNightVoter);
            skipNightVoters.add(skipNightVoter);
            user.sendMessage(Lang.SN_BACK_FROM_AFK.toComponentWithPrefix());
            user.sendMessage(voteButtons());
          }
        } else {
          if (isAfk) { // not in Active & Afk, change to afk
            afkSkipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          } else { // not in Active & Afk, change to active
            skipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_VOTE_STARTED.toComponentWithPrefix(Map.of("%player%", player.getName())));
            user.sendMessage(voteButtons());
          }
        }
      } else {
        if (skipNightVoters.contains(skipNightVoter)) { // not in world, in Active
          skipNightVoters.remove(skipNightVoter);
          int vote = skipNightVoter.resetVote();
          if (vote == 1) yes--;
          if (vote == -1) no--;
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
        if (afkSkipNightVoters.contains(skipNightVoter)) { // not in world, in Afk
          afkSkipNightVoters.remove(skipNightVoter);
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
      }
    }
    playerCount = skipNightVoters.size();
    afkCount = afkSkipNightVoters.size();
  }

  private List<SkipNightVoter> updateAll(List<SkipNightVoter> skipNightVoters, Component message) {
    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());
      User user = Main.getUser(player);
      boolean isAfk = Main.getUser(player.getUniqueId()).isAfk();

      if (isInOverworld(player)) {
        if (skipNightVoters.contains(skipNightVoter)) {
          skipNightVoter = skipNightVoters.get(skipNightVoters.indexOf(skipNightVoter));
          if (isAfk) { // in Active, change to Afk
            skipNightVoters.remove(skipNightVoter);
            afkSkipNightVoters.add(skipNightVoter);
            int vote = skipNightVoter.resetVote();
            if (vote == 1) yes--;
            if (vote == -1) no--;
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          }
        } else if (afkSkipNightVoters.contains(skipNightVoter)) {
          if (!isAfk) { // in Afk, change to Active
            afkSkipNightVoters.remove(skipNightVoter);
            skipNightVoters.add(skipNightVoter);
            user.sendMessage(Lang.SN_BACK_FROM_AFK.toComponentWithPrefix());
            user.sendMessage(voteButtons());
          }
        } else {
          if (isAfk) { // not in Active & Afk, change to afk
            afkSkipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          } else { // not in Active & Afk, change to active
            skipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_VOTE_STARTED.toComponentWithPrefix(Map.of("%player%", player.getName())));
            user.sendMessage(voteButtons());
          }
        }
        user.sendMessage(message);
      } else {
        if (skipNightVoters.contains(skipNightVoter)) { // not in world, in Active
          skipNightVoters.remove(skipNightVoter);
          int vote = skipNightVoter.resetVote();
          if (vote == 1) yes--;
          if (vote == -1) no--;
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
        if (afkSkipNightVoters.contains(skipNightVoter)) { // not in world, in Afk
          afkSkipNightVoters.remove(skipNightVoter);
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
      }
    }
    playerCount = skipNightVoters.size();
    afkCount = afkSkipNightVoters.size();
    return skipNightVoters;
  }

  private void updateAll(List<SkipNightVoter> skipNightVoters, Player sender) {
    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());
      User user = Main.getUser(player);
      boolean isAfk = Main.getUser(player.getUniqueId()).isAfk();

      if (isInOverworld(player)) {
        if (skipNightVoters.contains(skipNightVoter)) {
          skipNightVoter = skipNightVoters.get(skipNightVoters.indexOf(skipNightVoter));
          if (isAfk) { // in Active, change to Afk
            skipNightVoters.remove(skipNightVoter);
            afkSkipNightVoters.add(skipNightVoter);
            int vote = skipNightVoter.resetVote();
            if (vote == 1) yes--;
            if (vote == -1) no--;
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          }
        } else if (afkSkipNightVoters.contains(skipNightVoter)) {
          if (!isAfk) { // in Afk, change to Active
            afkSkipNightVoters.remove(skipNightVoter);
            skipNightVoters.add(skipNightVoter);
            user.sendMessage(Lang.SN_BACK_FROM_AFK.toComponentWithPrefix());
            user.sendMessage(voteButtons());
          }
        } else {
          if (isAfk) { // not in Active & Afk, change to afk
            afkSkipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_VOTE_STARTED.toComponentWithPrefix(Map.of("%player%", player.getName())));
            user.sendMessage(Lang.SN_AFK.toComponentWithPrefix());
          } else { // not in Active & Afk, change to active
            skipNightVoters.add(skipNightVoter);
            player.showBossBar(bar);
            user.sendMessage(Lang.SN_VOTE_STARTED.toComponentWithPrefix(Map.of("%player%", player.getName())));
            if (player == sender) {
              skipNightVoter.voteYes();
              user.sendMessage(Lang.SN_VOTED_SELF.toComponentWithPrefix(Map.of("%status%", "yes")));
            } else if (player.isSleeping()) {
              skipNightVoter.voteYes();
              user.sendMessage(Lang.SN_VOTED_SELF.toComponentWithPrefix(Map.of("%status%", "yes")));
            } else {
              user.sendMessage(voteButtons());
            }
          }
        }
      } else {
        if (skipNightVoters.contains(skipNightVoter)) { // not in world, in Active
          skipNightVoters.remove(skipNightVoter);
          int vote = skipNightVoter.resetVote();
          if (vote == 1) yes--;
          if (vote == -1) no--;
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
        if (afkSkipNightVoters.contains(skipNightVoter)) { // not in world, in Afk
          afkSkipNightVoters.remove(skipNightVoter);
          player.hideBossBar(bar);
          user.sendMessage(Lang.SN_LEFT_WORLD.toComponentWithPrefix());
        }
      }
    }
    playerCount = skipNightVoters.size();
    afkCount = afkSkipNightVoters.size();
  }

  private void actionBarMessage(Component message) {
    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
      SkipNightVoter skipNightVoter = new SkipNightVoter(player.getUniqueId());

      if (isInOverworld(player)) {
        if (skipNightVoters.contains(skipNightVoter)) {
          player.sendActionBar(message);
        }
      }
    }
  }

  private boolean voteCancel() {
    return (skipNightVoteType == SkipNightVoteType.NIGHT && (world.getTime() > 23900 || world.getTime() < 12516));
  }

  private enum Timer {
    INIT,
    OPERATION,
    INTERRUPT,
    CANCEL,
    FINAL,
    COMPLETE,
    COOLDOWN,
    OFF
  }

  private Component voteButtons() {
    return Lang.PREFIX.toComponent().asComponent().append(MiniMessage.miniMessage().deserialize("Please vote: " +
        "<green><bold><click:run_command:/skipnight yes>" +
        "<hover:show_text:'<gold><bold>Click here to vote yes'>" +
        "[Yes]</hover></click> " +
        "<dark_red><bold><click:run_command:/skipnight no>" +
        "<hover:show_text:'<gold><bold>Click here to vote no'>" +
        "[No]"));
  }

  private Component currentVotePA(int yes, int no, int afk) {
    // Current Vote: Yes - X No - X Idle - X Away - X
    return MiniMessage.miniMessage().deserialize("Current vote: " +
        " <bold><green>Yes<reset> - " + yes +
        " <bold><dark_red>No<reset> - " + no +
        " <bold><dark_aqua>Afk<reset> - " + afk);
  }

  private Component noVoteInProgress() {
    return Lang.PREFIX.toComponent().append(MiniMessage.miniMessage().deserialize(MessageFormat.format("<red>No vote in progress! <blue>{0}",
        "<click:suggest_command:/skipnight>" +
            "<hover:show_text:'<gold><bold>Click here to start a vote'>" +
            "<bold>[Start Vote]")));
  }
}
