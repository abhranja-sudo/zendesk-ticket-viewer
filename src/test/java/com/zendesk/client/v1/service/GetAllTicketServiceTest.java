package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Files;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.GetAllTicketResponse;
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
import java.util.ArrayList;
import java.util.List;

import static com.zendesk.client.v1.Config.baseUrl;
import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllTicketServiceTest {
    private static final int TICKET_PER_PAGE = 25;
    private TicketRetriever ticketRetriever;
    private ObjectMapper objectMapper;
    private Controller controller;
    GetAllTicketService getAllTicketService;

    @BeforeEach
    void setUp() {
        ticketRetriever = mock(TicketRetriever.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        controller = new Controller();
        getAllTicketService = new GetAllTicketService(controller, ticketRetriever, objectMapper);
        controller.changeServiceState(getAllTicketService);
    }

    /**
     *  Simulate the flow when total count of tickets is more than TICKET_PER_PAGE.
     *  During initialization, the only valid input is "GET_ALL_TICKETS". This initial call sets the uri for next API call.
     *
     *  After URI is set, testing the service with "NEXT" input.
     */
    @Test
    void whenHavingMoreDataOnNextPage() throws IOException, InterruptedException, URISyntaxException {

        String JsonMockResponse = Files.toString(new File("src/test/resources/hasMoreTicketOnNextPage.json"),
                StandardCharsets.UTF_8);

        when(ticketRetriever.retrieve(buildGetAllTicketURI()))
                .thenReturn(JsonMockResponse);


        GetAllTicketResponse expectedResponse = objectMapper.readValue(JsonMockResponse, GetAllTicketResponse.class);

        Frame expectedFrame = buildAllTicketFrame(expectedResponse.getTicketList());

        String input = Input.GET_ALL_TICKETS.getValue();
        Frame actualFrame = getAllTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        // State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetAllTicketService.class);


        String UriNextPage = "https://zccar.zendesk.com/api/v2/tickets.json?page%5Bafter%5D=eyJvIjoibmlj" +
                "ZV9pZCIsInYiOiJhUmtBQUFBQUFBQUEifQ%3D%3D&page%5Bsize%5D=25";

        when(ticketRetriever.retrieve(URI.create(UriNextPage)))
                .thenReturn(JsonMockResponse);

        expectedFrame = buildAllTicketFrame(expectedResponse.getTicketList());

        String nextInput = Input.NEXT.getValue();
        actualFrame = getAllTicketService.execute(nextInput);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        // State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetAllTicketService.class);
    }

    @Test
    void whenCalledWithGetAllTicketInputHavingNoDataOnNextPage() throws IOException, InterruptedException, URISyntaxException{

        //when total count of tickets is less than TICKET_PER_PAGE

        String JsonMockResponse = Files.toString(new File("src/test/resources/noTicketOnNextPage.json"),
                StandardCharsets.UTF_8);

        when(ticketRetriever.retrieve(buildGetAllTicketURI()))
                .thenReturn(JsonMockResponse);

        GetAllTicketResponse expectedResponse = objectMapper.readValue(JsonMockResponse, GetAllTicketResponse.class);

        Frame expectedFrame = buildAllTicketFrameForEndPage(expectedResponse.getTicketList());

        String input = Input.GET_ALL_TICKETS.getValue();

        Frame actualFrame = getAllTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        // State should transition to MenuService after last page
        Assertions.assertEquals(controller.getServiceState().getClass(), MenuService.class);

    }

    @Test
    void whenCalledWithMenuInput() {
        Frame expectedFrame = buildMenuFrame();

        String input = Input.MENU.getValue();

        Frame actualFrame = getAllTicketService.execute(input);

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

        // State should transition to MenuService after last page
        Assertions.assertEquals(controller.getServiceState().getClass(), MenuService.class);
    }

    @Test
    void whenMenuServiceCalledWithInvalidInput() {

        List<String> validInput = List.of(Input.GET_ALL_TICKETS.getValue(),
                Input.MENU.getValue(), Input.NEXT.getValue());

        List<String> invalidInput = new ArrayList<>();

        // add all invalid alphanumeric to invalid list and then test with each input
        for(char i = 'a'; i <='z'; i++ ) {
            String input = Character.toString(i);
            if(!validInput.contains(input))
                invalidInput.add(input);
        }

        for(int i = 0; i <= 9; i++ ) {
            String input = Integer.toString(i);
            if(!validInput.contains(input))
                invalidInput.add(input);
        }

        Frame expectedFrame = MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(INVALID_INPUT)
                        .getNext(GET_NEXT_VIEW)
                        .getAllTickets(START_PAGING_AGAIN)
                        .menu(MENU_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();

        for(String input: invalidInput) {
            Frame actualFrame = getAllTicketService.execute(input);
            assertThat(actualFrame)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedFrame);
        }

        // State shouldn't change
        Assertions.assertEquals(controller.getServiceState().getClass(), GetAllTicketService.class);
    }

    private MenuFrame buildMenuFrame() {
        return MenuFrame.builder()
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
    }

    private URI buildGetAllTicketURI() throws URISyntaxException {

        return new URIBuilder(baseUrl + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(TICKET_PER_PAGE))
                .build();
    }

    private GetAllTicketFrame buildAllTicketFrameForEndPage(List<Ticket> ticketList) {

        Header header = Header.builder()
                .appName(APP_NAME_VIEW)
                .build();

        Footer footer = Footer.builder()
                .customMessage(END_OF_PAGE)
                .getAllTickets(GET_ALL_TICKET_VIEW)
                .getTicket(GET_TICKET_VIEW)
                .quit(QUIT_VIEW)
                .build();

        return GetAllTicketFrame.builder()
                .header(header)
                .footer(footer)
                .ticketList(new TicketListViewer(ticketList))
                .build();
    }

    private GetAllTicketFrame buildAllTicketFrame(List<Ticket> ticketList) {

        Header header = Header.builder()
                .appName(APP_NAME_VIEW)
                .build();

        Footer footer = Footer.builder()
                .getAllTickets(START_PAGING_AGAIN)
                .menu(MENU_VIEW)
                .quit(QUIT_VIEW)
                .getNext(GET_NEXT_VIEW)
                .build();

        return GetAllTicketFrame.builder()
                .header(header)
                .footer(footer)
                .ticketList(new TicketListViewer(ticketList))
                .build();
    }
}