/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.tickets;


import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
public class TicketHandler {

  private static final Set<Ticket> tickets = new HashSet<>();

  public TicketHandler() {

    editCreationMessage();
    initTicketCreationListeners();

    for (RegularServerChannel channel : TicketType.MINECRAFT_SUPPORT.getCatagory().getChannels()) {
      Ticket.registerTicket(channel);
    }

    for (RegularServerChannel channel : TicketType.BUG_REPORT.getCatagory().getChannels()) {
      Ticket.registerTicket(channel);
    }

    for (RegularServerChannel channel : TicketType.DISCORD_SUPPORT.getCatagory().getChannels()) {
      Ticket.registerTicket(channel);
    }

    for (RegularServerChannel channel : TicketType.APPLICATION.getCatagory().getChannels()) {
      Ticket.registerTicket(channel);
    }

    for (RegularServerChannel channel : TicketType.APPEAL.getCatagory().getChannels()) {
      Ticket.registerTicket(channel);
    }

    new BukkitRunnable() {
      @Override
      public void run() {
        editCreationMessage();
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 20L * 60 * 5, 20L * 60 * 5);
  }

  public Set<Ticket> getTickets() {
    return tickets;
  }

  public static void addTicket(Ticket ticket) {
    tickets.add(ticket);
  }

  public static void createCreationMessage(@NotNull ServerTextChannel channel) {
    MessageBuilder messageBuilder = createCreationMessageBuilder();

    messageBuilder.send(channel).thenAccept(message -> {
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationMessageId", message.getId());
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationChannelId", channel.getId());
      DiscordHandler.getInstance().saveConfig();
    });

  }

  private void editCreationMessage() {
    MessageBuilder messageBuilder = createCreationMessageBuilder();

    DiscordHandler.getInstance().getDiscordApi()
        .getTextChannelById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationChannelId"))
        .ifPresent(channel -> {
          channel.getMessageById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationMessageId"))
              .thenAccept(message -> {
                if (message == null) {
                  return;
                }
                message.delete();

                messageBuilder.send(channel).thenAccept(message1 -> {
                  DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationMessageId", message1.getId());
                  DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationChannelId", channel.getId());
                  DiscordHandler.getInstance().saveConfig();
                });
              });
        });
  }

  private static MessageBuilder createCreationMessageBuilder() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.decode("#3C89D0"));
    builder.setAuthor("Create a ticket");
    builder.setDescription("Need help? **Create ticket now!**\n" +
        "\n" +
        "**Currently opened tickets:** " + tickets.size());
    builder.setThumbnail("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQhjz07Tkt9fMph6TDf7c6jbwlGe3HEW0lUjthe1OqU_X_VKqDY");

    Button minecraftButton = Button.create("minecraftTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Minecraft Support Ticket");
    Button bugReportButton = Button.create("bugReportTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Bug Report Ticket");
    Button discordButton = Button.create("discordTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Discord Support Ticket");
    Button appealButton = Button.create("appealTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Appeal Ticket");
    Button applicationButton = Button.create("applicationTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Application Ticket");

    MessageBuilder messageBuilder = new MessageBuilder();
    messageBuilder.setEmbed(builder);

    messageBuilder.addComponents(ActionRow.of(minecraftButton, bugReportButton, discordButton, appealButton, applicationButton));

    return messageBuilder;
  }

  private void initTicketCreationListeners() {
    DiscordHandler.getInstance().getDiscordApi().addMessageComponentCreateListener(e -> {
      MessageComponentInteraction messageComponentInteraction = e.getMessageComponentInteraction();
      Optional<ButtonInteraction> optionalButtonInteraction = messageComponentInteraction.asButtonInteraction();
      if (optionalButtonInteraction.isEmpty()) {
        return;
      }
      ButtonInteraction buttonInteraction = optionalButtonInteraction.get();
      String customId = buttonInteraction.getCustomId();
      if (messageComponentInteraction.getChannel().isEmpty()) {
        return;
      }
      TextChannel channel = messageComponentInteraction.getChannel().get();
      User user = buttonInteraction.getUser();

      Ticket ticket;
      switch (customId) {
        case "minecraftTicketCreate":
          if (getUserTicket(user, TicketType.MINECRAFT_SUPPORT) != null) {
            e.getMessageComponentInteraction().createImmediateResponder()
                .setContent("You already have a ticket open: <#" + getUserTicket(user, TicketType.MINECRAFT_SUPPORT).getServerTextChannel().getId() + ">")
                .setFlags(MessageFlag.EPHEMERAL).respond();
          }
          ticket = Ticket.createTicket(user, TicketType.MINECRAFT_SUPPORT);
          break;
        case "bugTicketCreate":
          if (getUserTicket(user, TicketType.BUG_REPORT) != null) {
            e.getMessageComponentInteraction().createImmediateResponder()
                .setContent("You already have a ticket open: <#" + getUserTicket(user, TicketType.BUG_REPORT).getServerTextChannel().getId() + ">")
                .setFlags(MessageFlag.EPHEMERAL).respond();
          }
          ticket = Ticket.createTicket(user, TicketType.BUG_REPORT);
          break;
        case "discordTicketCreate":
          if (getUserTicket(user, TicketType.DISCORD_SUPPORT) != null) {
            e.getMessageComponentInteraction().createImmediateResponder()
                .setContent("You already have a ticket open: <#" + getUserTicket(user, TicketType.DISCORD_SUPPORT).getServerTextChannel().getId() + ">")
                .setFlags(MessageFlag.EPHEMERAL).respond();
          }
          ticket = Ticket.createTicket(user, TicketType.DISCORD_SUPPORT);
          break;
        case "appealTicketCreate":
          if (getUserTicket(user, TicketType.APPEAL) != null) {
            e.getMessageComponentInteraction().createImmediateResponder()
                .setContent("You already have a ticket open: <#" + getUserTicket(user, TicketType.APPEAL).getServerTextChannel().getId() + ">")
                .setFlags(MessageFlag.EPHEMERAL).respond();
          }
          ticket = Ticket.createTicket(user, TicketType.APPEAL);
          break;
        case "applicationTicketCreate":
          if (getUserTicket(user, TicketType.APPLICATION) != null) {
            e.getMessageComponentInteraction().createImmediateResponder()
                .setContent("You already have a ticket open: <#" + getUserTicket(user, TicketType.APPLICATION).getServerTextChannel().getId() + ">")
                .setFlags(MessageFlag.EPHEMERAL).respond();
          }
          ticket = Ticket.createTicket(user, TicketType.APPLICATION);
          break;
        default:
          return;
      }

      e.getMessageComponentInteraction().createImmediateResponder()
          .setContent("Your ticket has been created: <#" + ticket.getServerTextChannel().getId() + ">")
          .setFlags(MessageFlag.EPHEMERAL).respond();

    });
  }

  private static Ticket getUserTicket(User user, TicketType ticketType) {
    for (Ticket ticket : tickets) {
      if (ticket.getCreator().equals(user) && ticket.getTicketType().equals(ticketType)) {
        return ticket;
      }
    }
    return null;
  }
}
