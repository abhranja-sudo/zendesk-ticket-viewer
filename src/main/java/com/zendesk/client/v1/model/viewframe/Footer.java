package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public final class Footer {

    private final String menu;
    private final String getAllTickets;
    private final String getTicket;
    private final String quit;
    private final String getNext;
    private final String customMessage;

    private Footer(String menu, String getAllTickets, String getTicket, String quit, String getNext, String errorMessage) {
        this.menu = menu;
        this.getAllTickets = getAllTickets;
        this.getTicket = getTicket;
        this.quit = quit;
        this.getNext = getNext;
        this.customMessage = errorMessage;
    }

    public static Footer.Builder builder() {
        return new Footer.Builder();
    }

    public static final class Builder {
        private String menu;
        private String getAllTickets;
        private String getTicket;
        private String quit;
        private String getNext;
        private String errorMessage;

        private Builder() {
        }

        public final Builder menu(String menu) {
            this.menu = Objects.requireNonNull(menu, "menu");
            return this;
        }

        public final Builder menu(Optional<String> menu) {
            this.menu = menu.orElse(null);
            return this;
        }


        public final Builder getAllTickets(String allTickets) {
            this.getAllTickets = Objects.requireNonNull(allTickets, "allTickets");
            return this;
        }


        public final Builder getAllTickets(Optional<String> allTickets) {
            this.getAllTickets = allTickets.orElse(null);
            return this;
        }

        public final Builder getTicket(String ticket) {
            this.getTicket = Objects.requireNonNull(ticket, "ticket");
            return this;
        }


        public final Builder getTicket(Optional<String> ticket) {
            this.getTicket = ticket.orElse(null);
            return this;
        }

        public final Builder quit(String quit) {
            this.quit = Objects.requireNonNull(quit, "quit");
            return this;
        }


        public final Builder quit(Optional<String> quit) {
            this.quit = quit.orElse(null);
            return this;
        }

        public final Builder getNext(String getNext) {
            this.getNext = Objects.requireNonNull(getNext, "quit");
            return this;
        }


        public final Builder getNext(Optional<String> getNext) {
            this.getNext = getNext.orElse(null);
            return this;
        }

        public final Builder customMessage(String errorMessage) {
            this.errorMessage = Objects.requireNonNull(errorMessage, "quit");
            return this;
        }


        public final Builder customMessage(Optional<String> errorMessage) {
            this.errorMessage = errorMessage.orElse(null);
            return this;
        }

        public Footer build() {
            return new Footer(menu, getAllTickets, getTicket, quit, getNext, errorMessage);
        }
    }



    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("\n");

        if (customMessage != null) {
            builder.append(customMessage + " \n\n");
        }

        if (getNext != null) {
            builder.append(getNext + " \n");
        }

        if (getAllTickets != null) {
            builder.append(getAllTickets +" \n");
        }

        if(getTicket != null) {
            builder.append(getTicket +" \n");
        }

        if (menu != null) {
            builder.append(menu +" \n");
        }

        if (quit != null) {
            builder.append(quit + " \n");
        }

        return builder.toString();
    }

}
