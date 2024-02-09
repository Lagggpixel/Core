/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.BotSlashCommand;
import me.lagggpixel.core.modules.discord.slashCommands.commands.LinkSlashCommand;
import me.lagggpixel.core.modules.discord.slashCommands.commands.TicketCreationSlashCommand;
import me.lagggpixel.core.modules.discord.slashCommands.commands.UnlinkSlashCommand;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.HashMap;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public class SlashCommandRegistry {

  private static SlashCommandRegistry INSTANCE;
  private final HashMap<Long, BotSlashCommand> slashCommands = new HashMap<>();

  public SlashCommandRegistry() {
    INSTANCE = this;
    initTask();
  }


  public void register(BotSlashCommand command) {
    slashCommands.put(command.getCommandId(), command);
  }

  public static SlashCommandRegistry getInstance() {
    return INSTANCE;
  }

  public void initTask() {
    new BukkitRunnable() {
      @Override
      public void run() {
        Main.getInstance().getLogger().info("Initializing slash commands");
        new LinkSlashCommand();
        new UnlinkSlashCommand();
        new TicketCreationSlashCommand();

        Main.getInstance().getLogger().info("Finished initializing slash commands");
        DiscordHandler.getInstance().getDiscordApi().addSlashCommandCreateListener(event -> {
          SlashCommandInteraction interaction = event.getSlashCommandInteraction();
          if (slashCommands.containsKey(interaction.getCommandId())) {
            slashCommands.get(interaction.getCommandId()).action(interaction);
          }
        });
        Main.getInstance().getLogger().info("Finished registering slash commands");
      }
    }.runTaskLater(Main.getInstance(), 0);
  }
}