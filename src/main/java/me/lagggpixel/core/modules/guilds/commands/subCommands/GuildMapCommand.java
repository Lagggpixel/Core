package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.ClaimProfile;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.data.Pillar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildMapCommand implements ISubCommand {

  private final GuildModule guildModule;
  private final ArrayList<UUID> showing = new ArrayList<>();
  private final HashSet<ClaimProfile> profiles = new HashSet<>();

  public GuildMapCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
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


  public void execute(CommandSender commandSender, String[] args) {

    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    if (!this.showing.contains(sender.getUniqueId())) {
      this.showing.add(sender.getUniqueId());
      int totalNearby = 0;
      for (Guild guild : guildModule.getGuildHandler().getGuilds()) {
        Random random = new Random();
        Material material = guildModule.getGuildHandler().getBlocks().get(random.nextInt(guildModule.getGuildHandler().getBlocks().size()));
        int messageSent = 0;
        int nearby = 0;
        for (Claim claim : guild.getClaims()) {
          Location c1 = new Location(claim.getWorld(), claim.getCornerOne().getBlockX(), sender.getLocation().getBlockY(), claim.getCornerOne().getBlockZ());
          Location c2 = new Location(claim.getWorld(), claim.getCornerTwo().getBlockX(), sender.getLocation().getBlockY(), claim.getCornerTwo().getBlockZ());
          Location c3 = new Location(claim.getWorld(), claim.getCornerThree().getBlockX(), sender.getLocation().getBlockY(), claim.getCornerThree().getBlockZ());
          Location c4 = new Location(claim.getWorld(), claim.getCornerFour().getBlockX(), sender.getLocation().getBlockY(), claim.getCornerFour().getBlockZ());
          if (claim.getWorld() == sender.getWorld() && (c1.distance(sender.getLocation()) < 70.0D || c2.distance(sender.getLocation()) < 70.0D || c3.distance(sender.getLocation()) < 70.0D || c4.distance(sender.getLocation()) < 70.0D)) {
            if (messageSent == 0) {
              messageSent = 1;
            }

            c1.setY(0.0D);
            c2.setY(0.0D);
            c3.setY(0.0D);
            c4.setY(0.0D);
            (new Pillar(getProfile(sender.getUniqueId()), material, (byte) 0, c1, claim.getOwner().getName() + " c1 " + sender.getName())).sendPillar();
            (new Pillar(getProfile(sender.getUniqueId()), material, (byte) 0, c2, claim.getOwner().getName() + " c2 " + sender.getName())).sendPillar();
            (new Pillar(getProfile(sender.getUniqueId()), material, (byte) 0, c3, claim.getOwner().getName() + " c3 " + sender.getName())).sendPillar();
            (new Pillar(getProfile(sender.getUniqueId()), material, (byte) 0, c4, claim.getOwner().getName() + " c4 " + sender.getName())).sendPillar();
            continue;
          }
          nearby++;
          totalNearby++;
        }

        if (nearby < guild.getClaims().size() && !guild.getClaims().isEmpty()) {
          sender.sendMessage(Lang.GUILD_MAP_DISPLAYED.toComponentWithPrefix(Map.of("%faction%", guild.getName(), "%block%", material.name())));
        }
      }
      if (totalNearby >= guildModule.getClaimManager().getClaims().size()) {
        sender.sendMessage(Lang.GUILD_MAP_NO_NEARBY.toComponentWithPrefix());
        this.showing.remove(sender.getUniqueId());
      }
    } else {
      this.showing.remove(sender.getUniqueId());
      sender.sendMessage(Lang.GUILD_MAP_HIDDEN.toComponentWithPrefix());
      for (Pillar pillar : guildModule.getPillarManager().getPillars()) {
        for (Guild guild : guildModule.getGuildHandler().getGuilds()) {
          if (pillar.getID().contains(guild.getName()) && pillar.getID().contains(sender.getName()) && (pillar.getID().contains("c1") || pillar.getID().contains("c2") || pillar.getID().contains("c3") || pillar.getID().contains("c4")))
            pillar.removePillar();
        }
      }
    }
  }

}
