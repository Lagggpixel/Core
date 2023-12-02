package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import org.bukkit.command.CommandSender;

public class GuildHelpCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildHelpCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender sender, String[] args) {
    // TODO: Implement guild help logic here
    // TODO: Provide information about available guild commands
  }
}
