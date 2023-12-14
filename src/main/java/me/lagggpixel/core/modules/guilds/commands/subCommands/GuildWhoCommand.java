package me.lagggpixel.core.modules.guilds.commands.subCommands;

import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.modules.guilds.GuildModule;
import me.lagggpixel.core.modules.guilds.commands.ISubCommand;
import me.lagggpixel.core.modules.guilds.data.Guild;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildWhoCommand implements ISubCommand {

  private final GuildModule guildModule;

  public GuildWhoCommand(GuildModule guildModule) {
    this.guildModule = guildModule;
  }

  public void execute(CommandSender commandSender, String[] args) {

    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return;
    }

    if (args.length == 0) {
      if (guildModule.getGuildHandler().getGuildFromPlayer(sender) == null) {
        sender.sendMessage(Lang.GUILD_NOT_IN_GUILD.toComponentWithPrefix());
      } else {
        for (Component msg : getInformation(guildModule.getGuildHandler().getGuildFromPlayer(sender))) {
          sender.sendMessage(msg);
        }
      }
      return;
    }

    StringBuilder sb = new StringBuilder();
    for (String arg : args) {
      sb.append(arg).append(" ");
    }
    String name = sb.toString().trim().replace(" ", "");

    if (guildModule.getGuildHandler().getGuildByName(name) == null) {
      sender.sendMessage(Lang.GUILD_GUILD_NOT_FOUND.toComponentWithPrefix(Map.of("%guild%", name)));
      return;
    }
    for (Component msg : getInformation(guildModule.getGuildHandler().getGuildByName(name))) {
      sender.sendMessage(msg);
    }

  }


  public List<Component> getInformation(Guild guild) {
    List<Component> list = new ArrayList<>();

    Map<String, String> placeholders = new HashMap<>();
    placeholders.put("%guild%", guild.getName());
    placeholders.put("%online%", guild.getOnlinePlayers().size() + "");
    placeholders.put("%total%", guild.getIDs().size() + "");
    if (guild.getHome() != null) {
      placeholders.put("%home-coords%", guild.getHome().getBlockX() + ", " + guild.getHome().getBlockZ());
    } else {
      placeholders.put("%home-coords%", "None");
    }
    StringBuilder officers = new StringBuilder();
    StringBuilder members = new StringBuilder();

    for (UUID ofs : guild.getOfficers()) {
      Player of = Bukkit.getPlayer(ofs);
      if (of != null) {
        officers.append(Lang.GUILD_WHO_SPLITTER.getDef()).append(Lang.GUILD_WHO_COLOR_ONLINE.getDef()).append(of.getName());
        continue;
      }
      OfflinePlayer oof = Bukkit.getOfflinePlayer(ofs);
      officers.append(Lang.GUILD_WHO_SPLITTER.getDef()).append(Lang.GUILD_WHO_COLOR_OFFLINE.getDef()).append(oof.getName());
    }

    for (UUID mems : guild.getMembers()) {
      Player mem = Bukkit.getPlayer(mems);
      if (mem != null) {
        members.append(Lang.GUILD_WHO_SPLITTER.getDef()).append(Lang.GUILD_WHO_COLOR_ONLINE.getDef()).append(mem.getName());
        continue;
      }
      OfflinePlayer omem = Bukkit.getOfflinePlayer(mems);
      members.append(Lang.GUILD_WHO_SPLITTER.getDef()).append(Lang.GUILD_WHO_COLOR_OFFLINE.getDef()).append(omem.getName());
    }

    if (!officers.isEmpty()) {
      placeholders.put("%officers%", officers.toString());
    }
    if (!members.isEmpty()) {
      placeholders.put("%members%", members.toString());
    }

    placeholders.put("%balance%", guild.getBalance() + "");

    Player leader = Bukkit.getPlayer(guild.getLeader());
    if (leader == null) {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(guild.getLeader());
      placeholders.put("%leader%", Lang.GUILD_WHO_COLOR_OFFLINE.getDef() + offlinePlayer.getName());
    } else {
      placeholders.put("{leader}", Lang.GUILD_WHO_COLOR_ONLINE.getDef() + leader.getName());
    }

    StringBuilder allies = new StringBuilder();

    if (!guild.getAllies().isEmpty()) {
      for (Guild allyGuilds : guild.getAllies()) {
        allies.append(Lang.GUILD_WHO_SPLITTER.getDef()).append(allyGuilds.getName());
      }

      if (!allies.isEmpty()) {
        placeholders.put("%allies%", allies.toString());
      }
    }

    list.add(Lang.GUILD_WHO_SPLITTER.toComponentWithPrefix(placeholders));
    list.add(Lang.GUILD_WHO_GUILD_INFO.toComponentWithPrefix(placeholders));
    list.add(Lang.GUILD_WHO_LEADER.toComponentWithPrefix(placeholders));
    if (officers.isEmpty()) {
      list.add(Lang.GUILD_WHO_OFFICERS.toComponentWithPrefix(placeholders));
    }
    if (!members.isEmpty()) {
      list.add(Lang.GUILD_WHO_MEMBERS.toComponentWithPrefix(placeholders));
    }
    list.add(Lang.GUILD_WHO_BALANCE.toComponentWithPrefix(placeholders));
    if (!allies.isEmpty()) {
      list.add(Lang.GUILD_WHO_ALLIES.toComponentWithPrefix(placeholders));
    }
    list.add(Lang.GUILD_WHO_SPLITTER.toComponentWithPrefix(placeholders));

    return list;

  }
}
