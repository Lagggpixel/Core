/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.slashCommands.commands;

import me.lagggpixel.core.modules.discord.slashCommands.CoreBotSlashCommand;
import me.lagggpixel.core.modules.discord.tickets.TicketHandler;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.Optional;

/**
 * @author Lagggpixel
 * @since February 09, 2024
 */
public class TicketCreationSlashCommand extends CoreBotSlashCommand {
  public TicketCreationSlashCommand() {
    super("ticketcreation", "Create a ticket creation message", null, true);
  }

  @Override
  public void action(SlashCommandInteraction interaction) {

    User user = interaction.getUser();
    Optional<Server> server = interaction.getServer();
    if (server.isEmpty()) {
      interaction.createImmediateResponder()
          .setContent("This command can only be used in a server")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }
    Optional<TextChannel> textChannel = interaction.getChannel();
    if (textChannel.isEmpty()) {
      interaction.createImmediateResponder()
          .setContent("This command can only be used in a text channel")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }
    Optional<ServerTextChannel> serverTextChannel = textChannel.get().asServerTextChannel();
    if (serverTextChannel.isEmpty()) {
      interaction.createImmediateResponder()
          .setContent("This command can only be used in a text channel")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    TicketHandler.createCreationMessage(serverTextChannel.get());

    interaction.createImmediateResponder()
        .setContent("Ticket creation message created")
        .setFlags(MessageFlag.EPHEMERAL).respond();

  }
}
