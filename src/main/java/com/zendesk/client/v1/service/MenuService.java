package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.Response;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class MenuService extends Service {

    private TicketRetriever ticketRetriever;
    private ObjectMapper objectMapper;
    private static final int TICKET_PER_PAGE = 25;

    public MenuService(Controller controller) {
        super(controller);
        ticketRetriever = new TicketRetriever();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public Frame execute(Input input) {
        if(input == Input.GET_ALL_TICKETS) {
            try {
                String body = ticketRetriever.retrieve(buildURI());
                Response response = objectMapper.readValue(body, Response.class);
                if(response.getMetaData().isHasMore()) {
                    String nextPageUrl = response.getLinks().getNext();
                    controller.changeServiceState(new GetAllTicketService(controller, nextPageUrl));
                }
                return buildTicketFrame(response.getTicketList());
            } catch (IOException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return  buildMenuFramewithError();

    }

    private MenuFrame buildMenuFramewithError() {
        return MenuFrame.builder()
                .header(Header.builder()
                        .greeting(GREETING)
                        .appName(APPNAME)
                        .build())
                .footer(Footer.builder()
                        .errorMessage("Invalid Input, Please try Again\n")
                        .allTickets(GETALLTICKET)
                        .quit(QUIT)
                        .build())
                .build();
    }

    private AllTicketFrame buildTicketFrame(List<Ticket> ticketList) {

        Header header = Header.builder()
                .appName(APPNAME)
                .build();

        Footer footer = Footer.builder()
                .menu(MENU)
                .quit(QUIT)
                .getNext(GET_NEXT)
                .build();

        return AllTicketFrame.builder()
                .header(header)
                .footer(footer)
                .ticketList(new TicketList(ticketList))
                .build();
    }

    private URI buildURI() throws URISyntaxException {

        return new URIBuilder(BASE_URL + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(TICKET_PER_PAGE))
                .build();

    }

}
