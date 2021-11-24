package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public class Footer {

    private final String menu;
    private final String allTickets;
    private final String quit;
    private final String getNext;
    private final String errorMessage;

    private Footer(String menu, String allTickets, String quit, String getNext, String errorMessage) {
        this.menu = menu;
        this.allTickets = allTickets;
        this.quit = quit;
        this.getNext = getNext;
        this.errorMessage = errorMessage;
    }

    public static Footer.Builder builder() {
        return new Footer.Builder();
    }


    public static final class Builder {
        private String menu;
        private String allTickets;
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


        public final Builder allTickets(String allTickets) {
            this.allTickets = Objects.requireNonNull(allTickets, "allTickets");
            return this;
        }


        public final Builder allTickets(Optional<String> allTickets) {
            this.allTickets = allTickets.orElse(null);
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

        public final Builder errorMessage(String errorMessage) {
            this.errorMessage = Objects.requireNonNull(errorMessage, "quit");
            return this;
        }


        public final Builder errorMessage(Optional<String> errorMessage) {
            this.errorMessage = errorMessage.orElse(null);
            return this;
        }

        public Footer build() {
            return new Footer(menu, allTickets, quit, getNext, errorMessage);
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("\n");

        if (errorMessage != null) {
            builder.append(errorMessage + " \n");
        }

        if (getNext != null) {
            builder.append(getNext + " \n");
        }

        if (menu != null) {
            builder.append(menu +" \n");
        }

        if (allTickets != null) {
            builder.append(allTickets +" \n");
        }

        if (quit != null) {
            builder.append(quit + " \n");
        }

        return builder.toString();
    }

}
