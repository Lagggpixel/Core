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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
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

    new BukkitRunnable() {
      @Override
      public void run() {
        initTicketListeners();
        editCreationMessage();
      }
    }.runTaskLater(Main.getInstance(), 0);
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
                message.edit(createCreationMessageEmbed()); // no .join() it will crash it .join() is added
              }).join();
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
    builder.setThumbnail("https://p7.hiclipart.com/preview/58/204/62/minecraft-mod-grass-block-computer-software-video-game-block.jpg");
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
      Main.log(Level.INFO, "TicketHandler:153, interaction recieved");
      MessageComponentInteraction messageComponentInteraction = e.getMessageComponentInteraction();
      Optional<ButtonInteraction> optionalButtonInteraction = messageComponentInteraction.asButtonInteraction();
      if (optionalButtonInteraction.isEmpty()) {
        Main.log(Level.WARNING, "Button interaction not found, TicketHandler:162");
        return;
      }
      ButtonInteraction buttonInteraction = optionalButtonInteraction.get();
      String customId = buttonInteraction.getCustomId();
      if (messageComponentInteraction.getChannel().isEmpty()) {
        Main.log(Level.WARNING, "Channel not found, TicketHandler:168");
        return;
      }
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
                  .respond()
                  .join();
              return;
            }
            messageComponentInteraction.createImmediateResponder()
                .setContent("Ticket closing...")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond()
                .join();
            ticket.close();
            return;
          }
          break;
        default:
          return;
      }

    });
  }

  private @NotNull Ticket handleMinecraftTicketCreate(@NotNull User user, MessageComponentInteraction e) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.setAuthor("Infinite Minecrafters Minecraft Support");
    embedBuilder.setDescription(
        "Hey <@" + user.getId() + ">,\n" +
            "Please provide us: \n" +
            "    **X** Your in game name(with prefix if you are on bedrock) \n" +
            "    **X** A detailed description of your issue/request \n" +
            "Our staff team will get back to you as soon as possible."
    );
    return Ticket.createTicket(user, TicketType.MINECRAFT_SUPPORT, embedBuilder);
  }

  private @NotNull Ticket handleBugReportTicketCreate(@NotNull User user) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.setAuthor("Infinite Minecrafters Bug Report");
    embedBuilder.setDescription(
        "Hey <@" + user.getId() + ">,\n" +
            "Please provide us: \n" +
            "    **X** Your in game name(with prefix if you are on bedrock) \n" +
            "    **X** A detailed description of the bug \n" +
            "Our staff team will get back to you as soon as possible."
    );
    return Ticket.createTicket(user, TicketType.BUG_REPORT, embedBuilder);
  }

  private @NotNull Ticket handleDiscordTicketCreate(@NotNull User user) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.setAuthor("Infinite Minecrafters Minecraft Support");
    embedBuilder.setDescription(
        "Hey <@" + user.getId() + ">,\n" +
            "Please provide us: \n" +
            "    **X** A detailed description of your issue/request\n" +
            "Our staff team will get back to you as soon as possible."
    );
    return Ticket.createTicket(user, TicketType.DISCORD_SUPPORT, embedBuilder);
  }

  private @NotNull Ticket handleAppealTicketCreate(@NotNull User user) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.setAuthor("Infinite Minecrafters Minecraft Support");
    embedBuilder.setDescription(
        "Hey <@" + user.getId() + ">,\n" +
            "Please provide us: \n" +
            "    **X** Your in game name(with prefix if you are on bedrock) \n" +
            "    **X** Type of punishment(kick, mute, ban) \n" +
            "    **X** Why do you think we should revoke your punishment \n" +
            "Our staff team will get back to you as soon as possible."
    );
    return Ticket.createTicket(user, TicketType.APPEAL, embedBuilder);
  }

  private @NotNull Ticket handleApplicationTicketCreate(@NotNull User user) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.GREEN);
    embedBuilder.setAuthor("Infinite Minecrafters Application");
    embedBuilder.setDescription(
        "Hey <@" + user.getId() + ">,\n" +
            "Please provide us: \n" +
            "    **X** Your in game name(with prefix if you are on bedrock) \n" +
            "    **X** Your age \n" +
            " **X** Your timezone \n" +
            "Our staff team will get back to you as soon as possible."
    );
    return Ticket.createTicket(user, TicketType.APPLICATION, embedBuilder);
  }

  private void handleTicketCreation(User user, @NotNull String ticketType, @NotNull MessageComponentCreateEvent e) {
    Ticket ticket = null;
    switch (ticketType) {
      case "minecraftTicketCreate":
        if (getUserTicket(user, TicketType.MINECRAFT_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.MINECRAFT_SUPPORT, e);
          return;
        }
        ticket = handleMinecraftTicketCreate(user, e.getMessageComponentInteraction());
        break;
      case "bugReportTicketCreate":
        if (getUserTicket(user, TicketType.BUG_REPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.BUG_REPORT, e);
          return;
        }
        ticket = handleBugReportTicketCreate(user);
        break;
      case "discordTicketCreate":
        if (getUserTicket(user, TicketType.DISCORD_SUPPORT) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.DISCORD_SUPPORT, e);
          return;
        }
        ticket = handleDiscordTicketCreate(user);
        break;
      case "appealTicketCreate":
        if (getUserTicket(user, TicketType.APPEAL) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPEAL, e);
          return;
        }
        ticket = handleAppealTicketCreate(user);
        break;
      case "applicationTicketCreate":
        if (getUserTicket(user, TicketType.APPLICATION) != null) {
          sendAlreadyHasTicketCreated(user, TicketType.APPLICATION, e);
          return;
        }
        ticket = handleApplicationTicketCreate(user);
        break;
    }
    if (ticket == null) {
      Main.log(Level.INFO, "Failed to create ticket");
      e.getMessageComponentInteraction().createImmediateResponder()
          .setContent("Failed to create ticket")
          .setFlags(MessageFlag.EPHEMERAL)
          .respond().join();
      return;
    }
    Main.log(Level.INFO, "Ticket created: " + ticket.getServerTextChannel().getName());
    e.getMessageComponentInteraction().createImmediateResponder()
        .setContent("Your ticket has been created: <#" + ticket.getServerTextChannel().getId() + ">")
        .setFlags(MessageFlag.EPHEMERAL)
        .respond().join();
  }

  private void sendAlreadyHasTicketCreated(User user, TicketType ticketType, @NotNull MessageComponentCreateEvent e) {
    e.getMessageComponentInteraction().createImmediateResponder()
        .setContent("You already have a ticket open: <#" + Objects.requireNonNull(getUserTicket(user, ticketType)).getServerTextChannel().getId() + ">")
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
    if (ticketAction.getCreator() == null) {
      builder.addField("Created By", "User Left The Server", true);
    } else {
      builder.addField("Created By", ticketAction.getCreator().getMentionTag(), true);
    }
    builder.addField("Ticket Type", StringUtils.capitalize(ticketAction.getTicket().getTicketType().name().toLowerCase().replace("_", " ")), true);
    if (ticketAction.getAction() == TicketAction.Action.RESOLVE) {
      assert ticketAction.getResolver() != null;
      builder.addField("Resolved By", ticketAction.getResolver().getName(), true);
      builder.addField("Reason", ticketAction.getReason(), true);
    } else if (ticketAction.getAction() == TicketAction.Action.CLOSE) {
      builder.addField("Reason", ticketAction.getReason(), true);
    }
    builder.setColor(ticketAction.getAction().getColor());
    if (ticketAction.getCreator() != null) {
      builder.setThumbnail(ticketAction.getCreator().getAvatar());
    }
    DiscordHandler.getInstance().TICKET_LOG_CHANNEL.ifPresent(textChannel -> {
      MessageBuilder messageBuilder = new MessageBuilder();
      messageBuilder.setEmbed(builder);
      messageBuilder.addActionRow(Button.link(generateTranscript(ticketAction.getTicket().getServerTextChannel()), "Transcript"));
      textChannel.sendMessage(builder);
    });
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
      return "https://paste.md-5.net/" + JsonParser.parseString(result).getAsJsonObject().get("key").getAsString();
    } catch (IOException e) {
      ExceptionUtils.handleException(e);
    }
    return "Connection with https://paste.md-5.net/ failed!";
  }

  public static void logTicketCreation(Ticket ticket) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.GREEN);
    builder.setTitle(TicketAction.Action.CREATE.getTitle());
    builder.addField("Created By", ticket.getCreator().getMentionTag(), true);
    builder.addField("Ticket Type", StringUtils.capitalize(ticket.getTicketType().name().replace("_", " ")), true);
    builder.setThumbnail(ticket.getCreator().getAvatar());
    DiscordHandler.getInstance().TICKET_LOG_CHANNEL
        .ifPresent(textChannel -> textChannel.sendMessage(builder));

  }
}
