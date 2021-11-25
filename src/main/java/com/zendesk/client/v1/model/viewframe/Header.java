package com.zendesk.client.v1.model.viewframe;

import java.util.Objects;
import java.util.Optional;

public final class Header {

    private final String greeting;
    private final String appName;

    private Header(String greeting, String appName) {
        this.greeting = greeting;
        this.appName = appName;
    }

    public static Header.Builder builder() {
        return new Header.Builder();
    }


    public static final class Builder {
        private String greeting;
        private String appName;

        private Builder() {
        }

        public final Header.Builder greeting(String greeting) {
            this.greeting = Objects.requireNonNull(greeting, "greeting");
            return this;
        }

        public final Header.Builder greeting(Optional<String> greeting) {
            this.greeting = greeting.orElse(null);
            return this;
        }


        public final Header.Builder appName(String appName) {
            this.appName = Objects.requireNonNull(appName, "appName");
            return this;
        }


        public final Header.Builder appName(Optional<String> appName) {
            this.appName = appName.orElse(null);
            return this;
        }

        public Header build() {
            return new Header(greeting, appName);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(" \n");
        if (greeting != null) {
            builder.append("   \n").append(greeting);
        }
        if (appName != null) {
//            if (builder.length() > 5) builder.append(", ");
            builder.append("   \n").append(appName);
        }
        return builder.toString();
    }

}
