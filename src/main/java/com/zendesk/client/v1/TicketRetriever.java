package com.zendesk.client.v1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Properties;


public class TicketRetriever {

    private static final String CONFIG_PATH = "src/main/resources/config.properties";
    private static final String JWT_KEY = "jwt.token";

    //Get JWT Token from properties file
    private static String bearerToken;
    static {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {

            Properties prop = new Properties();
            prop.load(input);
            bearerToken = prop.getProperty(JWT_KEY);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
