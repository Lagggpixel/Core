package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.data.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuildClaimCommand implements ISubCommand {
  private final GuildModule guildModule;
  
  public GuildClaimCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }
  
  @Override
  public void execute(CommandSender commandSender, String[] args) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }
    User senderUser = Main.getUser(sender.getUniqueId());
    Guild guild = guildModule.getGuildHandler().getGuildFromPlayerUUID(sender.getUniqueId());
    if (guild == null) {
      senderUser.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }
    if (guild.getMembers().contains(sender.getUniqueId())) {
      senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
      return;
    }
    
    sender.getInventory().remove(guildModule.getClaimManager().getWand());
    sender.getInventory().addItem(guildModule.getClaimManager().getWand());
  }
  
  @Override
  public List<String> tabComplete(CommandSender commandSender, String[] args) {
    return List.of(" ");
  }
}
