package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GuildHelpCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildHelpCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    // TODO: Implement guild help logic here
    // TODO: Provide information about available guild commands
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
