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

import lombok.Getter;
import me.lagggpixel.core.modules.discord.DiscordModule;
import me.lagggpixel.core.modules.discord.handlers.DiscordHandler;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.ExceptionLogger;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
@Getter
public class Ticket {

  private ServerTextChannel serverTextChannel;
  private final User creator;
  private TicketType ticketType;
  private final Set<User> additionalTicketMembers;

  private final Permissions everyonePermission =
      new PermissionsBuilder()
          .setAllDenied()
          .build();

  private final Permissions userPermission =
      new PermissionsBuilder()
          .setAllDenied()
          .setState(PermissionType.VIEW_CHANNEL, PermissionState.ALLOWED)
          .setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED)
          .setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED)
          .setState(PermissionType.ATTACH_FILE, PermissionState.ALLOWED)
          .build();

  private final Permissions staffPermission =
      new PermissionsBuilder()
          .setAllDenied()
          .setState(PermissionType.VIEW_CHANNEL, PermissionState.ALLOWED)
          .setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED)
          .setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED)
          .setState(PermissionType.ATTACH_FILE, PermissionState.ALLOWED)
          .build();

  private Ticket(@NotNull User creator, @NotNull TicketType ticketType) {
    this.creator = creator;
    this.ticketType = ticketType;
    this.additionalTicketMembers = new HashSet<>();

    ServerTextChannelBuilder builder = new ServerTextChannelBuilder(DiscordModule.discordHandler.server)
        .setName(buildTicketName())
        .setCategory(this.ticketType.getCatagory())
        .setTopic("Ticket created by " + creator.getDisplayName(DiscordModule.discordHandler.server) + " | " + ticketType.getId())
        .addPermissionOverwrite(DiscordModule.discordHandler.server.getEveryoneRole(), everyonePermission)
        .addPermissionOverwrite(creator, userPermission);

    // todo - add staff and admin permissions

    builder.create().thenAccept(serverTextChannel -> this.serverTextChannel = serverTextChannel).join();
  }

  private Ticket(ServerTextChannel channel) {
    this.serverTextChannel = channel;
    String name = this.serverTextChannel.getName();
    String type = name.split(" - ")[0];
    String username = name.split(" - ")[1];
    this.ticketType = TicketType.valueOf(type);
    this.creator = DiscordModule.discordHandler.server.getMembersByName(username).stream().toList().get(0);

    this.additionalTicketMembers = new HashSet<>();
    // todo - add additional ticket members
  }

  public static Ticket createTicket(User creator, TicketType ticketType) {
    Ticket ticket = new Ticket(creator, ticketType);
    ticket.sendCloseMessage();
    TicketHandler.addTicket(ticket);
    TicketHandler.editCreationMessage();
    return ticket;
  }

  public static Ticket createTicket(User creator, TicketType ticketType, EmbedBuilder embedBuilder) {
    Ticket ticket = new Ticket(creator, ticketType);
    ticket.serverTextChannel.sendMessage(embedBuilder).thenAccept(Message::pin);
    ticket.sendCloseMessage();
    TicketHandler.addTicket(ticket);
    TicketHandler.editCreationMessage();
    return ticket;
  }

  public static void registerTicket(RegularServerChannel channel) {
    Optional<ServerTextChannel> optionalServerTextChannel = channel.asServerTextChannel();
    if (optionalServerTextChannel.isEmpty()) {
      return;
    }
    ServerTextChannel serverTextChannel = optionalServerTextChannel.get();
    Ticket ticket = new Ticket(serverTextChannel);
    TicketHandler.addTicket(ticket);
  }

  public void setTicketType(@NotNull TicketType ticketType) {
    this.ticketType = ticketType;
    serverTextChannel.createUpdater()
        .setCategory(ticketType.getCatagory())
        .setName(buildTicketName())
        .update().join();
  }

  public void addAdditionalUser(User user) {
    additionalTicketMembers.add(user);
    serverTextChannel.createUpdater()
        .addPermissionOverwrite(user, userPermission)
        .update().join();
  }

  private String buildTicketName() {
    return ticketType.getId() + " - " + creator.getDisplayName(DiscordModule.discordHandler.server);
  }

  private void sendCloseMessage() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setColor(Color.ORANGE);
    builder.setAuthor("Click To Close Ticket", "", "https://i.imgur.com/84zmknE.png");
    builder.setDescription("Ticket created. Click the \uD83D\uDD12 button below to close this ticket.");
    new MessageBuilder()
        .setEmbed(builder)
        .addComponents(ActionRow.of(
            Button.secondary("closeTicket", "\uD83D\uDD12 Close Ticket")))
        .send(serverTextChannel);
  }

  public void close() {
    DiscordHandler.getInstance().getDiscordApi().getUserById(creator.getId()).thenAccept(user -> {
      user.openPrivateChannel().thenCompose(privateChannel -> privateChannel.sendMessage(
          new EmbedBuilder()
              .setAuthor("Ticket closed", "", "https://cdn.discordapp.com/embed/avatars/0.png")
              .setTitle("Enjoy your day!")
              .setDescription("Thanks for using our ticket system! If you got any other questions feel free to contact us again :)")));
      TicketHandler.logTicketAction(new TicketAction(user, this, TicketAction.Action.CLOSE));
    }).exceptionally(ExceptionLogger.get());
    TicketHandler.removeTicket(this);
    TicketHandler.editCreationMessage();
    serverTextChannel.delete().join();
  }
}
