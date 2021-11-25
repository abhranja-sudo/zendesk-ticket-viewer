package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public final class GetTicketFrame extends Frame{

    private final Header header;
    private final TicketViewer ticketViewer;
    private final Footer footer;

    private GetTicketFrame(Header header, TicketViewer ticketViewer, Footer footer) {
        this.header = header;
        this.ticketViewer = ticketViewer;
        this.footer = footer;
    }

    public static GetTicketFrame.Builder builder() {
        return new GetTicketFrame.Builder();
    }


    public static final class Builder {
        private Header header;
        private TicketViewer ticketViewer;
        private Footer footer;

        private Builder() {
        }

        public final GetTicketFrame.Builder header(Header header) {
            this.header = Objects.requireNonNull(header, "header");
            return this;
        }

        public final GetTicketFrame.Builder header(Optional<Header> header) {
            this.header = header.orElse(null);
            return this;
        }


        public final GetTicketFrame.Builder ticket(TicketViewer ticket) {
            this.ticketViewer = Objects.requireNonNull(ticket, "ticketViewer");
            return this;
        }


        public final GetTicketFrame.Builder ticket(Optional<TicketViewer> ticket) {
            this.ticketViewer = ticket.orElse(null);
            return this;
        }

        public final GetTicketFrame.Builder footer(Footer footer) {
            this.footer = Objects.requireNonNull(footer, "footer");
            return this;
        }


        public final GetTicketFrame.Builder footer(Optional<Footer> footer) {
            this.footer = footer.orElse(null);
            return this;
        }

        public GetTicketFrame build() {
            return new GetTicketFrame(header, ticketViewer, footer);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        if (header != null) {
            builder.append(header + "\n");
        }
        if (ticketViewer != null) {
            builder.append(ticketViewer + "\n");
        }
        if (footer != null) {
            builder.append(footer + "\n");
        }
        return builder.toString();
    }
}
