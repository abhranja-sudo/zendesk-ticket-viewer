package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public class AllTicketFrame extends Frame {
    private final Header header;
    private final TicketList ticketList;
    private final Footer footer;

    private AllTicketFrame(Header header, TicketList ticketList, Footer footer) {
        this.header = header;
        this.ticketList = ticketList;
        this.footer = footer;
    }

    public static AllTicketFrame.Builder builder() {
        return new AllTicketFrame.Builder();
    }


    public static final class Builder {
        private Header header;
        private TicketList ticketList;
        private Footer footer;

        private Builder() {
        }

        public final AllTicketFrame.Builder header(Header header) {
            this.header = Objects.requireNonNull(header, "header");
            return this;
        }

        public final AllTicketFrame.Builder header(Optional<Header> header) {
            this.header = header.orElse(null);
            return this;
        }


        public final AllTicketFrame.Builder ticketList(TicketList ticketList) {
            this.ticketList = Objects.requireNonNull(ticketList, "ticketListViewer");
            return this;
        }


        public final AllTicketFrame.Builder ticketList(Optional<TicketList> ticketList) {
            this.ticketList = ticketList.orElse(null);
            return this;
        }

        public final AllTicketFrame.Builder footer(Footer footer) {
            this.footer = Objects.requireNonNull(footer, "footer");
            return this;
        }


        public final AllTicketFrame.Builder footer(Optional<Footer> footer) {
            this.footer = footer.orElse(null);
            return this;
        }

        public AllTicketFrame build() {
            return new AllTicketFrame(header, ticketList, footer);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        if (header != null) {
            builder.append(header + "\n");
        }
        if (ticketList != null) {
            builder.append(ticketList + "\n");
        }
        if (footer != null) {
            builder.append(footer + "\n");
        }
        return builder.toString();
    }

}
