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
import me.lagggpixel.core.utils.ExceptionUtils;
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
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.*;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.ModalInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
public class TicketHandler {

  private static final Set<Ticket> tickets = new HashSet<>();

  public TicketHandler() {
    initTickets();
    initTicketListeners();

    new BukkitRunnable() {
      @Override
      public void run() {
        editCreationMessage();
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L * 60 * 5);
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
    createCreationMessageBuilder().send(channel).thenAccept(message -> {
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
                  Main.log(Level.WARNING, "Ticket creation message not found");
                  return;
                }
                message.edit(createCreationMessageEmbed()).join();
              });
        });
  }

  private static @NotNull MessageBuilder createCreationMessageBuilder() {
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

  private static @NotNull EmbedBuilder createCreationMessageEmbed() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.decode("#3C89D0"));
    builder.setAuthor("Create a ticket");
    builder.setDescription("Need help? **Create ticket now!**\n" +
        "\n" +
        "**Currently opened tickets:** `" + tickets.size() + "`");
    builder.setThumbnail("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQhjz07Tkt9fMph6TDf7c6jbwlGe3HEW0lUjthe1OqU_X_VKqDY");
    return builder;
  }

  private void initTickets() {
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
        case "minecraftTicketCreateModal":
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
            messageComponentInteraction.createImmediateResponder()
                .setContent("Ticket closing...")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
            ticket.close();
            return;
          }
          break;
        default:
          return;
      }

    });
    DiscordHandler.getInstance().getDiscordApi().addModalSubmitListener(e -> {
      ModalInteraction modalInteraction = e.getModalInteraction();
      String customId = modalInteraction.getCustomId();

      switch (customId) {
        case "minecraftTicketCreateModal":
          handleMinecraftTicketCreate(modalInteraction);
          break;
        case "bugReportTicketCreateModal":
          handleBugReportTicketCreate(modalInteraction);
          break;
        case "discordTicketCreateModal":
          handleDiscordTicketCreate(modalInteraction);
          break;
        case "appealTicketCreateModal":
          handleAppealTicketCreate(modalInteraction);
          break;
        case "applicationTicketCreateModal":
          handleApplicationTicketCreate(modalInteraction);
          break;
        default:
          return;
      }
    });
  }

  private void handleMinecraftTicketCreate(@NotNull ModalInteraction modalInteraction) {
    User user = modalInteraction.getUser();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.addField("**In Game Name**", modalInteraction.getTextInputValueByCustomId("ign").get(), false);
    embedBuilder.addField("**How can we help you**", modalInteraction.getTextInputValueByCustomId("message").get(), false);
    Ticket.createTicket(user, TicketType.MINECRAFT_SUPPORT, embedBuilder);
  }

  private void handleBugReportTicketCreate(@NotNull ModalInteraction modalInteraction) {
    User user = modalInteraction.getUser();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.addField("**In Game Name**", modalInteraction.getTextInputValueByCustomId("ign").get(), false);
    embedBuilder.addField("**Bug Description**", modalInteraction.getTextInputValueByCustomId("message").get(), false);
    Ticket.createTicket(user, TicketType.BUG_REPORT, embedBuilder);
  }

  private void handleDiscordTicketCreate(@NotNull ModalInteraction modalInteraction) {
    User user = modalInteraction.getUser();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.addField("**How can we help you**", modalInteraction.getTextInputValueByCustomId("message").get(), false);
    Ticket.createTicket(user, TicketType.DISCORD_SUPPORT, embedBuilder);
  }

  private void handleAppealTicketCreate(@NotNull ModalInteraction modalInteraction) {
    User user = modalInteraction.getUser();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.addField("**In Game Name**", modalInteraction.getTextInputValueByCustomId("ign").get(), false);
    embedBuilder.addField("**Punishment Type**", modalInteraction.getTextInputValueByCustomId("appealTicketCreateModalPunishType").get(), false);
    embedBuilder.addField("**Was the punishment false**", modalInteraction.getTextInputValueByCustomId("appealTicketCreatePunishJustified").get(), false);
    Ticket.createTicket(user, TicketType.BUG_REPORT, embedBuilder);
  }

  private void handleApplicationTicketCreate(ModalInteraction modalInteraction) {
    User user = modalInteraction.getUser();
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.addField("**In Game Name**", modalInteraction.getTextInputValueByCustomId("ign").get(), false);
    embedBuilder.addField("**Timezone**", modalInteraction.getTextInputValueByCustomId("timezone").get(), false);
    embedBuilder.addField("**Age**", modalInteraction.getTextInputValueByCustomId("age").get(), false);
    Ticket.createTicket(user, TicketType.BUG_REPORT, embedBuilder);
  }

  private void handleTicketCreation(User user, @NotNull String ticketType, MessageComponentCreateEvent e) {
    switch (ticketType) {
      case "minecraftTicketCreate":
        if (getUserTicket(user, TicketType.MINECRAFT_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.MINECRAFT_SUPPORT, e);
          return;
        }
        e.getInteraction().respondWithModal("minecraftTicketCreateModal", "Minecraft Support",
            ActionRow.of(
                TextInput.create(TextInputStyle.SHORT, "ign", "Your in game name"),
                TextInput.create(TextInputStyle.PARAGRAPH, "message", "How can we help you")
            ));
        break;
      case "bugReportTicketCreate":
        if (getUserTicket(user, TicketType.BUG_REPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.BUG_REPORT, e);
          return;
        }
        e.getInteraction().respondWithModal("bugReportTicketCreateModal", "Bug Report",
            ActionRow.of(
                TextInput.create(TextInputStyle.SHORT, "ign", "Your in game name"),
                TextInput.create(TextInputStyle.PARAGRAPH, "message", "Please give a description of the bug")
            ));
        break;
      case "discordTicketCreate":
        if (getUserTicket(user, TicketType.DISCORD_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.DISCORD_SUPPORT, e);
          return;
        }
        e.getInteraction().respondWithModal("discordTicketCreateModal", "Discord Support",
            ActionRow.of(
                TextInput.create(TextInputStyle.PARAGRAPH, "message", "How can we help you")
            ));
        break;
      case "appealTicketCreate":
        if (getUserTicket(user, TicketType.APPEAL) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPEAL, e);
          return;
        }
        e.getInteraction().respondWithModal("appealTicketCreateModal", "Appeal",
            ActionRow.of(
                TextInput.create(TextInputStyle.PARAGRAPH, "ign", "What is your in game name"),
                SelectMenu.createStringMenu("appealTicketCreateModalPunishType", "Appeal type",
                    List.of(SelectMenuOption.create("Kick", "kick"),
                        SelectMenuOption.create("Mute", "mute"),
                        SelectMenuOption.create("Ban", "ban"))),
                SelectMenu.createStringMenu("appealTicketCreatePunishJustified", "Was the punishment false",
                    List.of(SelectMenuOption.create("Yes", "true"),
                        SelectMenuOption.create("No", "false")))
            ));
        break;
      case "applicationTicketCreate":
        if (getUserTicket(user, TicketType.APPLICATION) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPLICATION, e);
          return;
        }
        e.getInteraction().respondWithModal("applicationTicketCreateModal", "Application",
            ActionRow.of(
                TextInput.create(TextInputStyle.SHORT, "ign", "What is your in game name"),
                TextInput.create(TextInputStyle.SHORT, "timezone", "Timezone(e.g. UTC)"),
                TextInput.create(TextInputStyle.SHORT, "age", "What is your age")
            ));
        break;
    }
  }

  private void sendAlreadyHasTicketCreated(User user, TicketType ticketType, @NotNull MessageComponentCreateEvent e) {
    e.getMessageComponentInteraction().createImmediateResponder()
        .setContent("You already have a ticket open: <#" + getUserTicket(user, ticketType).getServerTextChannel().getId() + ">")
        .setFlags(MessageFlag.EPHEMERAL).respond();
  }

  private static @Nullable Ticket getUserTicket(User user, TicketType ticketType) {
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

  private static @NotNull String generateTranscript(ServerTextChannel channel) {
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

  private static @NotNull String hasteBinPost(String data) {
    HttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost("https://paste.md-5.net/documents");

    try {
      post.setEntity(new StringEntity(data, StandardCharsets.UTF_8));

      HttpResponse response = client.execute(post);
      String result = EntityUtils.toString(response.getEntity());
      return "https://paste.md-5.net/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
    } catch (IOException e) {
      ExceptionUtils.handleException(e);
    }
    return "Connection with https://paste.md-5.net/ failed!";
  }
}
