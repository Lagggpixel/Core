package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.handlers.GuildHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class GuildCreateCommand implements ISubCommand {
  private final GuildModule guildModule;
  private final GuildHandler guildHandler;
  
  public GuildCreateCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
    guildHandler = this.guildModule.getGuildHandler();
  }
  
  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    UUID playerUniqueId = player.getUniqueId();

    if (args.length != 2) {
      sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix());
      return;
    }

    if (guildHandler.getGuildFromPlayerUUID(playerUniqueId) != null) {
      sender.sendMessage(Lang.GUILD_ALREADY_IN_GUILD.toComponentWithPrefix());
      return;
    }
    
    String guildName = args[1];
    
    if (guildName.length() < 3 || guildName.length() > 16 || !Character.isLetter(guildName.charAt(0))) {
      sender.sendMessage(Lang.GUILD_NAME_INVALID.toComponentWithPrefix());
      return;
    }
    
    for (Guild guild : guildHandler.getGuilds()) {
      if (guild.getName().equalsIgnoreCase(guildName)) {
        player.sendMessage(Lang.GUILD_NAM_EXISTS.toComponentWithPrefix());
        
        return;
      }
    }
    
    Guild newGuild = guildHandler.createGuild(guildName, player);
    if (newGuild == null) {
      sender.sendMessage(Lang.GUILD_FAILED_TO_CREATE.toComponentWithPrefix());
      return;
    }
    guildHandler.getGuilds().add(newGuild);
    sender.sendMessage(Lang.GUILD_CREATED.toComponentWithPrefix(Map.of("%guild%", newGuild.getName())));
  }
}
