package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import org.bukkit.command.CommandSender;

public class GuildKickCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildKickCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    // TODO: Implement guild kick logic here
    // TODO: Make sure to check permissions and handle edge cases
  }
}
