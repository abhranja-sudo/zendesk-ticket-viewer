package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public class MenuFrame extends Frame{

    private final Header header;
    private final Footer footer;

    private MenuFrame(Header header, Footer footer) {
        this.header = header;
        this.footer = footer;
    }

    public static MenuFrame.Builder builder() {
        return new MenuFrame.Builder();
    }


    public static final class Builder {
        private Header header;
        private Footer footer;

        private Builder() {
        }

        public final MenuFrame.Builder header(Header header) {
            this.header = Objects.requireNonNull(header, "header");
            return this;
        }

        public final MenuFrame.Builder header(Optional<Header> header) {
            this.header = header.orElse(null);
            return this;
        }

        public final MenuFrame.Builder footer(Footer footer) {
            this.footer = Objects.requireNonNull(footer, "footer");
            return this;
        }


        public final MenuFrame.Builder footer(Optional<Footer> footer) {
            this.footer = footer.orElse(null);
            return this;
        }

        public MenuFrame build() {
            return new MenuFrame(header, footer);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(" \n");
        if (header != null) {
            builder.append(header + "\n");
        }
        if (footer != null) {
            builder.append(footer + "\n");
        }
        return builder.toString();
    }
}
