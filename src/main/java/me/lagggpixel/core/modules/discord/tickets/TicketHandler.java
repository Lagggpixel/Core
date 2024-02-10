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


import com.google.gson.JsonParser;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
public class TicketHandler {

  private static final Set<Ticket> tickets = new HashSet<>();

  public TicketHandler() {

    editCreationMessage();
    initTicketListeners();

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

  public static void removeTicket(Ticket ticket) {
    tickets.remove(ticket);
  }

  public static void createCreationMessage(@NotNull ServerTextChannel channel) {
    MessageBuilder messageBuilder = createCreationMessageBuilder();

    messageBuilder.send(channel).thenAccept(message -> {
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationMessageId", message.getId());
      DiscordHandler.getInstance().getYamlConfiguration().set("ticketCreationChannelId", channel.getId());
      DiscordHandler.getInstance().saveConfig();
    });

  }

  public static void editCreationMessage() {
    DiscordHandler.getInstance().getDiscordApi()
        .getTextChannelById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationChannelId"))
        .ifPresent(channel -> {
          channel.getMessageById(DiscordHandler.getInstance().getYamlConfiguration().getString("ticketCreationMessageId"))
              .thenAccept(message -> {
                if (message == null) {
                  return;
                }
                message.edit(createCreationMessageEmbed()).join();
              });
        });
  }

  private static MessageBuilder createCreationMessageBuilder() {

    Button minecraftButton = Button.create("minecraftTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Minecraft Support Ticket");
    Button bugReportButton = Button.create("bugReportTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Bug Report Ticket");
    Button discordButton = Button.create("discordTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Discord Support Ticket");
    Button appealButton = Button.create("appealTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Appeal Ticket");
    Button applicationButton = Button.create("applicationTicketCreate", ButtonStyle.PRIMARY, "\uD83D\uDCE9 Application Ticket");

    MessageBuilder messageBuilder = new MessageBuilder();
    messageBuilder.setEmbed(createCreationMessageEmbed());

    messageBuilder.addComponents(ActionRow.of(minecraftButton, bugReportButton, discordButton, appealButton, applicationButton));

    return messageBuilder;
  }

  private static EmbedBuilder createCreationMessageEmbed() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.decode("#3C89D0"));
    builder.setAuthor("Create a ticket");
    builder.setDescription("Need help? **Create ticket now!**\n" +
        "\n" +
        "**Currently opened tickets:** " + tickets.size());
    builder.setThumbnail("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQhjz07Tkt9fMph6TDf7c6jbwlGe3HEW0lUjthe1OqU_X_VKqDY");
    return builder;
  }

  private void initTicketListeners() {
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
        case "bugReportTicketCreate":
        case "discordTicketCreate":
        case "appealTicketCreate":
        case "applicationTicketCreate":
          handleTicketCreation(user, customId, e);
          break;
        case "closeTicket":
          for (Ticket ticket : tickets) {
            if (buttonInteraction.getChannel().isEmpty() || ticket.getServerTextChannel().getId() != buttonInteraction.getChannel().get().getId()) {
              continue;
            }
            if (ticket.getCreator().getId() != buttonInteraction.getUser().getId()) {
              messageComponentInteraction.createImmediateResponder()
                  .setContent("You can't close this ticket, only the ticket creator can close it!")
                  .setFlags(MessageFlag.EPHEMERAL)
                  .respond();
              return;
            }
            ticket.close();
            return;
          }
          break;
        default:
          return;
      }

    });
  }

  private void handleTicketCreation(User user, @NotNull String ticketType, MessageComponentCreateEvent e) {
    Ticket ticket;
    switch (ticketType) {
      case "minecraftTicketCreate":
        if (getUserTicket(user, TicketType.MINECRAFT_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.MINECRAFT_SUPPORT, e);
          return;
        }
        ticket = Ticket.createTicket(user, TicketType.MINECRAFT_SUPPORT);
        break;
      case "bugTicketCreate":
        if (getUserTicket(user, TicketType.BUG_REPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.BUG_REPORT, e);
          return;
        }
        ticket = Ticket.createTicket(user, TicketType.BUG_REPORT);
        break;
      case "discordTicketCreate":
        if (getUserTicket(user, TicketType.DISCORD_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.DISCORD_SUPPORT, e);
          return;
        }
        ticket = Ticket.createTicket(user, TicketType.DISCORD_SUPPORT);
        break;
      case "appealTicketCreate":
        if (getUserTicket(user, TicketType.APPEAL) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPEAL, e);
          return;
        }
        ticket = Ticket.createTicket(user, TicketType.APPEAL);
        break;
      case "applicationTicketCreate":
        if (getUserTicket(user, TicketType.APPLICATION) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPLICATION, e);
          return;
        }
        ticket = Ticket.createTicket(user, TicketType.APPLICATION);
        break;
      default:
        return;
    }
    e.getMessageComponentInteraction().createImmediateResponder()
        .setContent("Your ticket has been created: <#" + ticket.getServerTextChannel().getId() + ">")
        .setFlags(MessageFlag.EPHEMERAL).respond();
  }

  private void sendAlreadyHasTicketCreated(User user, TicketType ticketType, MessageComponentCreateEvent e) {
    e.getMessageComponentInteraction().createImmediateResponder()
        .setContent("You already have a ticket open: <#" + getUserTicket(user, ticketType).getServerTextChannel().getId() + ">")
        .setFlags(MessageFlag.EPHEMERAL).respond();
  }

  private static Ticket getUserTicket(User user, TicketType ticketType) {
    for (Ticket ticket : tickets) {
      if (ticket.getCreator().equals(user) && ticket.getTicketType().equals(ticketType)) {
        return ticket;
      }
    }
    return null;
  }

  public static void logTicketAction(@NotNull TicketAction ticketAction) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(ticketAction.getAction().getColor());
    builder.setTitle(ticketAction.getAction().getTitle());
    //in case he left the server
    if (ticketAction.getCreator() == null) {
      builder.addField("Created By", "User Left The Server", true);
    } else {
      builder.addField("Created By", ticketAction.getCreator().getMentionTag(), true);
    }
    builder.addField("Ticket Type", ticketAction.getTicket().getTicketType().name().toLowerCase().replace("_", " "), true);
    if (ticketAction.getAction() == TicketAction.Action.RESOLVE) {
      assert ticketAction.getResolver() != null;
      builder.addField("Resolved By", ticketAction.getResolver().getName(), true);
      builder.addField("Reason", ticketAction.getReason(), true);
    } else if (ticketAction.getAction() == TicketAction.Action.CLOSE) {
      builder.addField("Reason", ticketAction.getReason(), true);
    }
    builder.addField("Transcript", generateTranscript(ticketAction.getTicket().getServerTextChannel()), true);
    builder.setColor(ticketAction.getAction().getColor());
    if (ticketAction.getCreator() != null) {
      builder.setThumbnail(ticketAction.getCreator().getAvatar());
    }
    DiscordHandler.getInstance().TICKET_LOG_CHANNEL.ifPresent(textChannel -> textChannel.sendMessage(builder));
  }

  private static String generateTranscript(ServerTextChannel channel) {
    List<Message> messages;

    try {
      messages = new ArrayList<>(channel.getMessages(100).get());
    } catch (Exception ex) {
      return "Failed to generate transcript (Garbage Collected Exception)";
    }
    boolean longerHistory = false;
    if (messages.isEmpty()) {
      return "Empty?";
    }
    Collections.reverse(messages);
    if (!messages.get(0).getAuthor().isBotUser()) {
      longerHistory = true;
    }
    StringBuilder chatLog = new StringBuilder();
    if (longerHistory) {
      chatLog.append("*** HISTORY LIMITED TO 100 MESSAGES ***").append("\n");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        .withLocale(Locale.ENGLISH)
        .withZone(ZoneId.systemDefault());
    for (Message message : messages) {
      String authorAndDate = "[" + formatter.format(message.getCreationTimestamp()) + "] " + message.getAuthor().getName() + ": ";
      String content = message.getContent();
      if (!message.getAttachments().isEmpty()) {
        content += " [+" + message.getAttachments().size() + " attachments uploaded]";
      }
      if (!message.getEmbeds().isEmpty()) {
        content += " [+" + message.getEmbeds().size() + " embeds included]";
      }
      chatLog.append(authorAndDate).append(content).append("\n");
    }
    return hasteBinPost(chatLog.toString());
  }

  private static String hasteBinPost(String data) {
    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost("https://paste.md-5.net/documents");

    try {
      post.setEntity(new StringEntity(data, StandardCharsets.UTF_8));

      HttpResponse response = client.execute(post);
      String result = EntityUtils.toString(response.getEntity());
      return "https://paste.md-5.net/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
    } catch(IOException e) {
      e.printStackTrace();
    }
    return "Connection with https://paste.md-5.net/ failed!";
  }
}
