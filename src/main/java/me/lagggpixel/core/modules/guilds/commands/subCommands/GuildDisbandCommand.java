package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import me.lagggpixel.core.modules.guilds.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class GuildDisbandCommand implements SubCommand {
  private final GuildModule guildModule;
  
  public GuildDisbandCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage("Only players can disband guilds!");
      return;
    }
    
    GuildHandler guildHandler = guildModule.getGuildHandler();
    UUID playerUniqueId = sender.getUniqueId();
    String guildName = guildHandler.getGuildFromPlayerUUID(playerUniqueId).getName();
    
    if (guildName == null) {
      commandSender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }
    
    Guild guild = guildHandler.getGuildFromGuildName(guildName);
    if (!guild.getLeader().equals(playerUniqueId)) {
      commandSender.sendMessage(Lang.GUILD_NOT_LEADER.toComponentWithPrefix());
      return;
    }
    
    guildHandler.disbandGuild(sender, guild);
    
    commandSender.sendMessage(Lang.GUILD_DISBANDED_LEADER.toComponentWithPrefix(Map.of("%guild%", guildName)));
  }
}
