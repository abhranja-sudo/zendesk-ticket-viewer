package com.zendesk.client.v1;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.zendesk.client.v1.Config.bearerToken;


public class TicketRetriever {

    public String retrieve(URI uri) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .GET()
                .build();

        HttpResponse<String> response = HttpClientFactory.getInstance()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if(isStatus2xx(response))
            return response.body();

        throw new ZendeskResponseException(response.statusCode(), response.body());
    }


    private boolean isStatus2xx(HttpResponse response) {
        return response.statusCode() / 100 == 2;
    }
}
