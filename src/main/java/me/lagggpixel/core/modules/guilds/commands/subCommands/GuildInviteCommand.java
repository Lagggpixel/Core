package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.interfaces.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildInviteCommand implements ISubCommand {
  private final GuildModule guildModule;

  public GuildInviteCommand(GuildModule guildModule) {
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

    if (guildModule.getGuildHandler().getGuildFromPlayer(sender) == null) {
      senderUser.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      return;
    }

    if (guildModule.getGuildHandler().getGuildFromPlayer(sender) != null) {
      Guild guild = guildModule.getGuildHandler().getGuildFromPlayer(sender);

      if (!(guild.getOfficers().contains(sender.getUniqueId()) || guild.isLeader(sender.getUniqueId()))) {
        senderUser.sendMessage(Lang.GUILD_MUST_BE_OFFICER.toComponentWithPrefix());
        return;
      }

      StringBuilder sb = new StringBuilder();
      for (String arg : args) {
        sb.append(arg).append(" ");
      }
      String name = sb.toString().trim().replace(" ", "");

      Player inv = Bukkit.getPlayer(name);

      if (inv == null) {
        senderUser.sendMessage(Lang.PLAYER_NOT_FOUND.toComponentWithPrefix(Map.of("%player%", name)));
        return;
      }

      if (guild.getPlayers().contains(Bukkit.getOfflinePlayer(inv.getUniqueId()))) {
        senderUser.sendMessage(Lang.GUILD_INVITE_PLAYER_ALREADY_JOINED.toComponentWithPrefix(Map.of("%player%", inv.getName())));
        return;
      }
      if (guild.getInvitedPlayers().contains(inv.getUniqueId())) {
        senderUser.sendMessage(Lang.GUILD_INVITE_PLAYER_ALREADY_INVITED.toComponentWithPrefix(Map.of("%player%", inv.getName())));
        return;
      }
      TextComponent component = Lang.GUILD_INVITED_PLAYER_NOTIFY.toTextComponentWithPrefix(Map.of("%player%", sender.getName(), "%guild%", guild.getName()));

      component = component.toBuilder()
          .hoverEvent(HoverEvent.showText(Lang.GUILD_INVITED_PLAYER_TOOLTIP.toComponent()))
          .clickEvent(ClickEvent.runCommand("/guild join " + guild.getName()))
          .build();
      inv.sendMessage(component);
      
      senderUser.sendMessage(Lang.GUILD_INVITED_PLAYER_ACKNOWLEDGE.toComponentWithPrefix(Map.of("%player%", inv.getName())));
      guild.sendMessage(Lang.GUILD_INVITED_PLAYER_BROADCAST.toComponentWithPrefix(Map.of("%player%", sender.getName(), "%target%", inv.getName())));
      guild.getInvitedPlayers().add(inv.getUniqueId());
    }
  }
}
