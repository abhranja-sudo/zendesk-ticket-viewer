package com.zendesk.client.v1;

import org.apache.http.client.utils.URIBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Properties;


public class TicketRetriever {

    private static final String BASE_URL = "https://zccar.zendesk.com/api/v2/tickets.json/";
    private static final String API_VERSION = "api/v2/";

    //Get JWT Token from properties file
    private static String bearerToken;
    {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            bearerToken = prop.getProperty("jwt.token");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String retrieve(URI uri) throws URISyntaxException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if(isStatus2xx(response))
            return response.body();

        throw new ZendeskResponseException(response.statusCode(), response.body());
    }


    private boolean isStatus2xx(HttpResponse response) {
        return response.statusCode() / 100 == 2;
    }

    String retrieveAllTickets(int ticketPerPage, int pageNumber, String path) throws URISyntaxException, IOException, InterruptedException {

        URI uri = new URIBuilder(BASE_URL + API_VERSION + path )
                .addParameter("per_page", Integer.toString(ticketPerPage))
                .addParameter("page", Integer.toString(pageNumber))
                .build();

        return retrieve(uri);
    }
}
