package me.lagggpixel.core.modules.discord.tickets;

import org.javacord.api.entity.channel.RegularServerChannel;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
public class TicketHandler {

  private static Set<Ticket> tickets;

  public TicketHandler() {

    tickets = new HashSet<>();

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

}
