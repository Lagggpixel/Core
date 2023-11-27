package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.modules.guilds.managers.GuildManager;
import me.lagggpixel.core.modules.guilds.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCreateCommand implements SubCommand {
  private final GuildModule guildModule;
  
  public GuildCreateCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender sender, String[] args) {
    // Check if the sender is a player
    if (!(sender instanceof Player player)) {
      sender.sendMessage("Only players can create guilds!");
      return;
    }
    
    // Check if the player is already in a guild
    GuildManager guildManager = GuildModule.getInstance().getGuildManager();
    UUID playerUniqueId = player.getUniqueId();
    
    if (guildManager.getGuildId(playerUniqueId) != null) {
      sender.sendMessage("You are already in a guild!");
      return;
    }
    
    // Check if the correct number of arguments is provided
    if (args.length != 2) {
      sender.sendMessage("Usage: /guild create <name>");
      return;
    }
    
    String guildName = args[1];
    
    // Validate guild name (add more validation as needed)
    if (guildName.length() < 3 || guildName.length() > 16) {
      sender.sendMessage("Guild name must be between 3 and 16 characters.");
      return;
    }
    
    // Create a new guild
    Guild newGuild = guildManager.createGuild(guildName, playerUniqueId);
    
    // Additional logic such as broadcasting, sending messages, etc.
    sender.sendMessage("Guild '" + guildName + "' created successfully!");
  }
}
