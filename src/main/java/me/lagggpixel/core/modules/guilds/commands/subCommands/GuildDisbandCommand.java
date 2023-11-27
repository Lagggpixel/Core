package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class GuildDisbandCommand implements SubCommand {
  private final GuildModule guildModule;
  
  public GuildDisbandCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender sender, String[] args) {
    // TODO: Implement guild disband logic here
    // TODO: Make sure to check permissions and handle edge cases
  }
}
