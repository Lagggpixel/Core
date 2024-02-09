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


import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
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

  private static Set<Ticket> tickets;

  public TicketHandler() {

    tickets = new HashSet<>();

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

  }

  public Set<Ticket> getTickets() {
    return tickets;
  }

  public static void addTicket(Ticket ticket) {
    tickets.add(ticket);
  }

  public static void createCreationMessage(@NotNull ServerTextChannel channel) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.decode("#3C89D0"));
    builder.setAuthor("Create Support Ticket - React Emoji", "https://plugily.xyz", "https://i.imgur.com/yqKqqTX.png");
    builder.setDescription("Need help with bug? **Create ticket now!**\n" +
        "\n" +
        "React one of the following emojis to create ticket for:\n" +
        " **Minecraft Support Ticket**\n" +
        " **Bug Report Ticket**\n" +
        " **Discord Support Ticket**\n" +
        " **Appeal Ticket**\n" +
        " **Application ticket**\n" +
        "\n" +
        "**Currently opened tickets:** " + tickets.size());
    builder.setThumbnail("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQhjz07Tkt9fMph6TDf7c6jbwlGe3HEW0lUjthe1OqU_X_VKqDY");

    Button minecraftButton = Button.create("minecraftTicketCreate", ButtonStyle.PRIMARY, "Minecraft Support Ticket");
    Button bugReportButton = Button.create("bugReportTicketCreate", ButtonStyle.PRIMARY, "Bug Report Ticket");
    Button discordButton = Button.create("discordTicketCreate", ButtonStyle.PRIMARY, "Discord Support Ticket");
    Button appealButton = Button.create("appealTicketCreate", ButtonStyle.PRIMARY, "Appeal Ticket");
    Button applicationButton = Button.create("applicationTicketCreate", ButtonStyle.PRIMARY, "Application Ticket");

    MessageBuilder messageBuilder = new MessageBuilder();
    messageBuilder.setEmbed(builder);

    messageBuilder.addComponents(ActionRow.of(minecraftButton, bugReportButton, discordButton, appealButton, applicationButton));

    messageBuilder.send(channel).thenAccept(message -> {
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationMessageId", message.getId());
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationChannelId", channel.getId());
      DiscordHandler.getInstance().saveConfig();
    });

  }

  private void editCreationMessage() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.decode("#3C89D0"));
    builder.setAuthor("Create Support Ticket - React Emoji", "https://plugily.xyz", "https://i.imgur.com/yqKqqTX.png");
    builder.setDescription("Need help with bug? **Create ticket now!**\n" +
        "\n" +
        "React one of the following emojis to create ticket for:\n" +
        " **Minecraft Support Ticket**\n" +
        " **Bug Report Ticket**\n" +
        " **Discord Support Ticket**\n" +
        " **Appeal Ticket**\n" +
        " **Application ticket**\n" +
        "\n" +
        "**Currently opened tickets:** " + tickets.size());
    builder.setThumbnail("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQhjz07Tkt9fMph6TDf7c6jbwlGe3HEW0lUjthe1OqU_X_VKqDY");
    DiscordHandler.getInstance().getDiscordApi()
        .getTextChannelById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationChannelId"))
        .ifPresent(channel -> {
          channel.getMessageById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationMessageId"))
              .thenAccept(message -> {
                if (message == null) {
                  return;
                }
                message.edit(builder);
              });
        });
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

      switch (customId) {
        case "minecraftTicketCreate":
          createMinecraftTicket(user);
          break;
        case "bugTicketCreate":
          createBugReportTicket(user);
          break;
        case "discordTicketCreate":
          createDiscordSupportTicket(user);
          break;
        case "appealTicketCreate":
          createAppealTicket(user);
          break;
        case "applicationTicketCreate":
          createApplicationTicket(user);
          break;
      }


    });
  }


  private static void createMinecraftTicket(User creator) {

  }

  private static void createBugReportTicket(User creator) {

  }

  private static void createDiscordSupportTicket(User creator) {

  }

  private static void createAppealTicket(User creator) {

  }

  private static void createApplicationTicket(User creator) {

  }
}
