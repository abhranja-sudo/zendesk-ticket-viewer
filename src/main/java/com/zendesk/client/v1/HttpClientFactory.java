package com.zendesk.client.v1;

import java.net.http.HttpClient;

/**
 * Created factory to decouples HttpClient from TicketRetriever, to make TicketRetriever testable
 */
public class HttpClientFactory {

    private static HttpClient instance;

    public static void setInstance(HttpClient client) {
        instance = client;
    }

    public static HttpClient getInstance() {
        if (instance == null) {
            return HttpClient.newHttpClient();
        }

        return instance;
    }
}
