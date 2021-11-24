package com.zendesk.client.v1.model.viewframe;

import com.zendesk.client.v1.model.ticket.Ticket;

import java.util.List;

public class TicketList {

    private List<Ticket> ticketList;

    public TicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%5s %60s %15s %15s %35s", "Id", "Subject", "Status", "Priority", "Updated_At"));
        builder.append("\n\n");

        for (Ticket ticket: ticketList) {
            builder.append(String.format("%5s %60s %15s %15s %35s", ticket.getId(), ticket.getSubject(), ticket.getStatus(),
                    ticket.getPriority(), ticket.getDateTime() ));
            builder.append("\n");

        }
        return builder.toString();
    }
}
