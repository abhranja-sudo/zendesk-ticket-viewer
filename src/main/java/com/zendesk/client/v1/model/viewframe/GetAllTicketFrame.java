package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public class GetAllTicketFrame extends Frame {
    private final Header header;
    private final TicketListViewer ticketListViewer;
    private final Footer footer;

    private GetAllTicketFrame(Header header, TicketListViewer ticketListViewer, Footer footer) {
        this.header = header;
        this.ticketListViewer = ticketListViewer;
        this.footer = footer;
    }

    public static GetAllTicketFrame.Builder builder() {
        return new GetAllTicketFrame.Builder();
    }


    public static final class Builder {
        private Header header;
        private TicketListViewer ticketListViewer;
        private Footer footer;

        private Builder() {
        }

        public final GetAllTicketFrame.Builder header(Header header) {
            this.header = Objects.requireNonNull(header, "header");
            return this;
        }

        public final GetAllTicketFrame.Builder header(Optional<Header> header) {
            this.header = header.orElse(null);
            return this;
        }


        public final GetAllTicketFrame.Builder ticketList(TicketListViewer ticketListViewer) {
            this.ticketListViewer = Objects.requireNonNull(ticketListViewer, "ticketListViewer");
            return this;
        }


        public final GetAllTicketFrame.Builder ticketList(Optional<TicketListViewer> ticketList) {
            this.ticketListViewer = ticketList.orElse(null);
            return this;
        }

        public final GetAllTicketFrame.Builder footer(Footer footer) {
            this.footer = Objects.requireNonNull(footer, "footer");
            return this;
        }


        public final GetAllTicketFrame.Builder footer(Optional<Footer> footer) {
            this.footer = footer.orElse(null);
            return this;
        }

        public GetAllTicketFrame build() {
            return new GetAllTicketFrame(header, ticketListViewer, footer);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        if (header != null) {
            builder.append(header + "\n");
        }
        if (ticketListViewer != null) {
            builder.append(ticketListViewer + "\n");
        }
        if (footer != null) {
            builder.append(footer + "\n");
        }
        return builder.toString();
    }

}
