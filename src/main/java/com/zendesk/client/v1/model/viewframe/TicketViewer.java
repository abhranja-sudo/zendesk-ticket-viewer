package com.zendesk.client.v1.model.viewframe;

import com.zendesk.client.v1.model.ticket.Ticket;

public class TicketViewer {

    private Ticket ticket;

    public TicketViewer(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%5s %60s %15s %15s %35s", "Id", "Subject", "Status", "Priority", "Updated_At"));
        builder.append("\n\n");

        builder.append(String.format("%5s %60s %15s %15s %35s", ticket.getId(), ticket.getSubject(), ticket.getStatus(),
                ticket.getPriority(), ticket.getUpdatedAt() ));

        return builder.toString();
    }
}
