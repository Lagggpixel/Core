package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.managers.GuildManager;
import me.lagggpixel.core.modules.guilds.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class GuildCreateCommand implements SubCommand {
  private final GuildModule guildModule;
  
  public GuildCreateCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    
    GuildManager guildManager = GuildModule.getInstance().getGuildManager();
    UUID playerUniqueId = player.getUniqueId();
    
    if (guildManager.getGuildName(playerUniqueId) != null) {
      sender.sendMessage(Lang.GUILD_ALREADY_IN_GUILD.toComponentWithPrefix());
      return;
    }
    
    if (args.length != 2) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }
    
    String guildName = args[1];
    
    if (guildName.length() < 3 || guildName.length() > 16 || !Character.isLetter(guildName.charAt(0))) {
      sender.sendMessage(Lang.GUILD_NAME_INVALID.toComponentWithPrefix());
      return;
    }
    
    Guild newGuild = guildManager.createGuild(guildName, player);
    if (newGuild == null) {
      sender.sendMessage(Lang.GUILD_FAILED_TO_CREATE.toComponentWithPrefix());
      return;
    }
    sender.sendMessage(Lang.GUILD_CREATED.toComponentWithPrefix(Map.of("%guild%", newGuild.getName())));
  }
}
