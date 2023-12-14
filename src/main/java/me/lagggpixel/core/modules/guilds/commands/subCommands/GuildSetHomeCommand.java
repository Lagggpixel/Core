package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildSetHomeCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildSetHomeCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {

    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);

    if (guild == null) {
      commandSender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    if (guild.isLeader(sender.getUniqueId()) || guild.getOfficers().contains(sender.getUniqueId())) {
      Location location = sender.getLocation();
      for (Claim claim : guild.getClaims()) {
        if (claim.isInside(location, true)) {
          guild.setHome(location);
          sender.sendMessage(Lang.GUILD_SETHOME_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%x%", location.getBlockX() + "", "%y%", location.getBlockY() + "", "%z%", location.getBlockZ() + "")));
          guild.sendMessage(Lang.GUILD_SETHOME_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%x%", location.getBlockX() + "", "%y%", location.getBlockY() + "", "%z%", location.getBlockZ() + "")));
          return;
        }
      }
      sender.sendMessage(Lang.GUILD_SETHOME_NOT_IN_CLAIM.toComponentWithPrefix());
    } else {
      sender.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
    }

  }
}
