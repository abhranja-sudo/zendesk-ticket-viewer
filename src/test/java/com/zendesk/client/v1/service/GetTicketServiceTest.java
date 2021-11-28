package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Files;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.ZendeskResponseException;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getticketresponse.GetTicketResponse;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static com.zendesk.client.v1.ConfigLoader.BASE_URL;
import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetTicketServiceTest {

    private TicketRetriever ticketRetriever;
    private ObjectMapper objectMapper;
    private Controller controller;
    private GetTicketService getTicketService;
    private static final String NO_INPUT = "No input. Please try entering again";
    private static final String INVALID_INPUT = "Invalid input. Please try again entering ID in positive Integer format";
    private static final String RECORD_NOT_FOUND = "Record Not Found. Please try with another ticket ID";


    @BeforeEach
    void setUp() {
        ticketRetriever = mock(TicketRetriever.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        controller = new Controller();
        getTicketService = new GetTicketService(controller, ticketRetriever, objectMapper);
        controller.changeServiceState(getTicketService);
    }

    @Test
    void whenCalledWithValidTicketId() throws URISyntaxException, IOException, InterruptedException {
        String input = "3";
        URI uri = buildURI(input);
        String JsonMockResponse = Files.toString(new File("src/test/resources/singleTicket.json"),
                StandardCharsets.UTF_8);

        when(ticketRetriever.retrieve(uri))
                .thenReturn(JsonMockResponse);

        GetTicketResponse expectedResponse = objectMapper.readValue(JsonMockResponse, GetTicketResponse.class);
        Frame expectedFrame = buildTicketFrame(expectedResponse.getTicket());

        Frame actualFrame = getTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        //State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetTicketService.class);
    }

    @Test
    void whenCalledWithInvalidInput() throws URISyntaxException, IOException, InterruptedException {
        String input = "dsd";
        URI uri = buildURI(input);

        String JsonMockResponse = Files.toString(new File("src/test/resources/BadRequestBody.json"),
                StandardCharsets.UTF_8);

        when(ticketRetriever.retrieve(uri))
                .thenThrow(new ZendeskResponseException(400, JsonMockResponse));

        Frame expectedFrame = buildMenuFrameWithError(INVALID_INPUT);

        Frame actualFrame = getTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        //State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetTicketService.class);

    }

    @Test
    void whenCalledWithInputWhoseRecordIsNotFound() throws URISyntaxException, IOException, InterruptedException {
        String input = "103";
        URI uri = buildURI(input);

        String JsonMockResponse = Files.toString(new File("src/test/resources/RecordNotFound.json"),
                StandardCharsets.UTF_8);

        when(ticketRetriever.retrieve(uri))
                .thenThrow(new ZendeskResponseException(404, JsonMockResponse));

        Frame expectedFrame = buildMenuFrameWithError(RECORD_NOT_FOUND);

        Frame actualFrame = getTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        //State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetTicketService.class);
    }

    @Test
    void whenCalledWithMenuInput() {
        String input = Input.MENU.getValue();

        Frame expectedFrame = MenuFrame.builder()
                .header(Header.builder()
                        .greeting(GREETING_VIEW)
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .getAllTickets(GET_ALL_TICKET_VIEW)
                        .getTicket(GET_TICKET_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();

        Frame actualFrame = getTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        // State should transition to MenuService
        Assertions.assertEquals(controller.getServiceState().getClass(), MenuService.class);
    }

    @Test
    void whenNoTicketIdIsPassed() {
        String input = "";

        Frame expectedFrame = buildMenuFrameWithError(NO_INPUT);

        Frame actualFrame = getTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        //State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetTicketService.class);
    }

    private GetTicketFrame buildTicketFrame(Ticket ticket) {
        Header header = Header.builder()
                .appName(APP_NAME_VIEW)
                .build();

        Footer footer = Footer.builder()
                .customMessage(ENTER_TICKET_ID)
                .quit(QUIT_VIEW)
                .menu(MENU_VIEW)
                .build();

        return GetTicketFrame.builder()
                .header(header)
                .footer(footer)
                .ticket(new TicketViewer(ticket))
                .build();
    }

    private URI buildURI(String id) throws URISyntaxException {
        return new URIBuilder(BASE_URL + API_VERSION + GET_TICKET + id)
                .build();
    }

    private MenuFrame buildMenuFrameWithError(String errorMessage) {
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(errorMessage)
                        .menu(MENU_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();
    }
}