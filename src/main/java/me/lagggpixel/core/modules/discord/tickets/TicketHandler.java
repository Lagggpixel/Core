package me.lagggpixel.core.modules.discord.tickets;

import org.javacord.api.entity.channel.RegularServerChannel;

import java.util.Set;

/**
 * @author Lagggpixel
 * @since January 27, 2024
 */
public class TicketHandler {

  private static Set<Ticket> tickets;

  public TicketHandler() {

    for (RegularServerChannel channel : TicketType.MINECRAFT_SUPPORT.getCatagory().getChannels()) {

    }

  }

  public Set<Ticket> getTickets() {
    return tickets;
  }

  public static void addTicket(Ticket ticket) {
    tickets.add(ticket);
  }

}
