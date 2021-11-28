package com.zendesk.client.v1;

import com.google.common.io.Files;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;

import static com.zendesk.client.v1.Config.baseUrl;
import static com.zendesk.client.v1.Path.API_VERSION;
import static com.zendesk.client.v1.Path.GET_ALL_TICKETS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketRetrieverTest {

    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static final String JWT_KEY = "jwt.token";
    private HttpClient client;
    private TicketRetriever ticketRetriever;


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

    @BeforeEach
    void setup() {
       client = mock(HttpClient.newHttpClient().getClass());
       ticketRetriever = new TicketRetriever();
    }

    @Test
    void testIfTokenIsLoadingFromPropertiesFile() {

        String expectedJwt = "bearer XXXXXX";

        String actualJwt = bearerToken;
        assertEquals(expectedJwt, actualJwt);

    }

    @Test
    void testResponseIf2xx() throws IOException, URISyntaxException, InterruptedException {

        String JsonMockResponseBody = Files.toString(new File("src/test/resources/singleTicket.json"),
                StandardCharsets.UTF_8);

        HttpRequest request = buildRequest();

        HttpClientFactory.setInstance(client);

        when(client.send(any((request.getClass())), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mock2XXResponse(request, JsonMockResponseBody));

        String expectedResponseBody = JsonMockResponseBody;
        String actualResponseBody = ticketRetriever.retrieve(buildGetAllTicketURI());

        assertEquals(expectedResponseBody, actualResponseBody);

    }

    @Test
    void testResponseIf4xx() throws IOException, URISyntaxException, InterruptedException {

        String JsonMockResponseBody = Files.toString(new File("src/test/resources/BadRequestBody.json"),
                StandardCharsets.UTF_8);

        HttpRequest request = buildRequest();

        HttpClientFactory.setInstance(client);

        when(client.send(any((request.getClass())), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mock4XXResponse(request, JsonMockResponseBody));


        Assertions.assertThrows(ZendeskResponseException.class,
                () -> ticketRetriever.retrieve(buildGetAllTicketURI()));

    }

    private HttpRequest buildRequest() throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(buildGetAllTicketURI())
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .GET()
                .build();
    }

    private URI buildGetAllTicketURI() throws URISyntaxException {

        return new URIBuilder(baseUrl + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(25))
                .build();
    }

    private HttpResponse<String> mock2XXResponse(HttpRequest httpRequest, String body) {
        return new HttpResponse<>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return httpRequest;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return httpRequest.headers();
            }

            @Override
            public String body() {
                return body;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return httpRequest.uri();
            }

            @Override
            public HttpClient.Version version() {
                return httpRequest.version().get();
            }
        };

    }

    private HttpResponse<String> mock4XXResponse(HttpRequest httpRequest, String body) {
        return new HttpResponse<>() {
            @Override
            public int statusCode() {
                return 400;
            }

            @Override
            public HttpRequest request() {
                return httpRequest;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return httpRequest.headers();
            }

            @Override
            public String body() {
                return body;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return httpRequest.uri();
            }

            @Override
            public HttpClient.Version version() {
                return httpRequest.version().get();
            }
        };

    }
}