package com.zendesk.client.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.*;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class GetAllTicketService extends Service {

    private String url;
    private final TicketRetriever ticketRetriever;
    private final ObjectMapper objectMapper;
    private static final int TICKET_PER_PAGE = 25;

    //Dependency Injection through constructor (instead of creating objects inside) for easily testable code (TDD)
    public GetAllTicketService(Controller controller, TicketRetriever ticketRetriever, ObjectMapper objectMapper) {
        super(controller);
        this.ticketRetriever = ticketRetriever;
        this.objectMapper = objectMapper;
    }

    @Override
    public Frame execute(String input) {

        Input menuInput = Input.getInput(input);

        if(menuInput == Input.GET_ALL_TICKETS) {
            String body;
            try {
                body = ticketRetriever.retrieve(buildGetAllTicketURI());
                return processBody(body);

            } catch (IOException | InterruptedException | URISyntaxException e) {
                return processFatalException();
            }
        }


        if(menuInput == Input.NEXT) {
            try {
                String body = ticketRetriever.retrieve(URI.create(url));
                return processBody(body);

            } catch (IOException | InterruptedException e) {
                return processFatalException();
            }

        }

        else if(menuInput == Input.MENU) {
            controller.changeServiceState(new MenuService(controller, new GetAllTicketService(controller,
                    ticketRetriever, objectMapper),
                    new GetTicketService(controller, ticketRetriever, objectMapper)));
            return buildMenuFrame();
        }

        //For any other Invalid Input
        return buildMenuFrameWithError();
    }

    private Frame processFatalException() {
        controller.changeServiceState(new MenuService(controller, new GetAllTicketService(controller, ticketRetriever, objectMapper),
                new GetTicketService(controller, ticketRetriever, objectMapper)));
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(FATAL_PROGRAM_ERROR)
                        .getAllTickets(GET_NEXT_VIEW)
                        .getTicket(GET_TICKET_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();
    }

    private Frame processBody(String body) throws JsonProcessingException {

        GetAllTicketResponse response = objectMapper.readValue(body, GetAllTicketResponse.class);
        if(!response.getMetaData().isHasMore()) {
            controller.changeServiceState(new MenuService(controller, new GetAllTicketService(controller, ticketRetriever, objectMapper),
                    new GetTicketService(controller, ticketRetriever, objectMapper)));
            return buildAllTicketFrameForEndPage(response.getTicketList());
        }
        url = response.getLinks().getNext();
        return buildAllTicketFrame(response.getTicketList());
    }

    private MenuFrame buildMenuFrameWithError() {
        return MenuFrame.builder()
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

    private URI buildGetAllTicketURI() throws URISyntaxException {

        return new URIBuilder(BASE_URL + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(TICKET_PER_PAGE))
                .build();
    }

}
