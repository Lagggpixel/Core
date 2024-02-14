/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.slashCommands;

import me.lagggpixel.core.interfaces.BotSlashCommand;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import me.lagggpixel.core.modules.discord.handlers.SlashCommandRegistry;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public class CoreBotSlashCommand implements BotSlashCommand {
  private SlashCommand slashCommand;
  private boolean global;

  private final String name;
  private final String description;
  private final List<SlashCommandOption> options;

  public CoreBotSlashCommand(String name, String description, List<SlashCommandOption> options) {
    this.name = name;
    this.description = description;
    this.options = options;
    register();
  }

  public CoreBotSlashCommand(String name, String description, List<SlashCommandOption> options, boolean admin) {
    this.name = name;
    this.description = description;
    this.options = options;
    registerAdmin();
  }

  public void register() {
    SlashCommandBuilder slashCommandbuilder = SlashCommand.with(name, description);

    if (options != null && !options.isEmpty()) {
      options.forEach(slashCommandbuilder::addOption);
    }

    slashCommand = slashCommandbuilder.createGlobal(DiscordHandler.getInstance().getDiscordApi()).join();
    global = true;
    SlashCommandRegistry.getInstance().register(this);
  }

  public void registerAdmin() {
    SlashCommandBuilder slashCommandbuilder = SlashCommand.with(name, description);

    if (options != null && !options.isEmpty()) {
      options.forEach(slashCommandbuilder::addOption);
    }
    slashCommandbuilder.setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR);

    slashCommand = slashCommandbuilder.createGlobal(DiscordHandler.getInstance().getDiscordApi()).join();
    global = true;
    SlashCommandRegistry.getInstance().register(this);
  }

  @Override
  public SlashCommand getSlashCommand() {
    return slashCommand;
  }

  @Override
  public long getCommandId() {
    return slashCommand.getId();
  }

  @Override
  public boolean isGlobal() {
    return global;
  }

  @Override
  public void action(SlashCommandInteraction interaction) {
    // The slash command wasn't ran in a server somehow?
    if (interaction.getServer().isEmpty()) {
      return;
    }

    // The slash command wasn't ran in a channel somehow?
    if (interaction.getChannel().isEmpty()) {
      return;
    }
  }
}
