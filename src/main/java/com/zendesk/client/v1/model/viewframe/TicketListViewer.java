package com.zendesk.client.v1.model.viewframe;

import com.zendesk.client.v1.model.ticket.Ticket;

import java.util.List;

public final class TicketListViewer {

    private List<Ticket> ticketList;

    public TicketListViewer(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%5s %60s %15s %15s %35s", "Id", "Subject", "Status", "Priority", "Updated_At"));
        builder.append("\n\n");

        for (Ticket ticket: ticketList) {
            builder.append(String.format("%5s %60s %15s %15s %35s", ticket.getId(), ticket.getSubject(), ticket.getStatus(),
                    ticket.getPriority(), ticket.getUpdatedAt() ));
            builder.append("\n");

        }
        return builder.toString();
    }
}
