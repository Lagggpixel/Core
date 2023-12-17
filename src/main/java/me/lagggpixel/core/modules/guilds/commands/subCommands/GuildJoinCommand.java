package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildJoinCommand implements ISubCommand {

  GuildModule guildModule;

  public GuildJoinCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    User senderUser = Main.getUser(sender.getUniqueId());

    if (args.length == 0) {
      senderUser.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    if (guildModule.getGuildHandler().getGuildFromPlayer(sender) != null) {
      senderUser.sendMessage(Lang.GUILD_ALREADY_IN_GUILD.toComponentWithPrefix());
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (String arg : args) {
      sb.append(arg).append(" ");
    }
    String name = sb.toString().trim().replace(" ", "");

    Guild guild = guildModule.getGuildHandler().getGuildByName(name);

    if (guild == null) {
      senderUser.sendMessage(Lang.GUILD_GUILD_NOT_FOUND.toComponentWithPrefix(Map.of("%guild%", name)));
      return;
    }

    if (guild.getPlayers().size() >= guildModule.getGuildHandler().getMaxPlayers()) {
      senderUser.sendMessage(Lang.GUILD_JOIN_TOO_MANY_PLAYERS.toComponentWithPrefix(Map.of("%guild%", name)));
      return;
    }

    if (!guild.getInvitedPlayers().contains(sender.getUniqueId())) {
      senderUser.sendMessage(Lang.GUILD_JOIN_NOT_INVITED.toComponentWithPrefix(Map.of("%guild%", name)));
      return;
    }
    
    senderUser.sendMessage(Lang.GUILD_JOIN_PLAYER_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%guild%", name)));
    guild.sendMessage(Lang.GUILD_JOIN_PLAYER_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName())));
    guild.getInvitedPlayers().remove(sender.getUniqueId());
    guild.getMembers().add(sender.getUniqueId());
  }
}
