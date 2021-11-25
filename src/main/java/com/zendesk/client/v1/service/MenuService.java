package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.GetAllTicketResponse;
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
    public Frame execute(String input) {
        Input menuInput = Input.valueOfInput(input);
        if(menuInput == Input.GET_ALL_TICKETS) {
            try {
                String body = ticketRetriever.retrieve(buildGetAllTicketURI());
                GetAllTicketResponse response = objectMapper.readValue(body, GetAllTicketResponse.class);
                if(response.getMetaData().isHasMore()) {
                    String nextPageUrl = response.getLinks().getNext();
                    controller.changeServiceState(new GetAllTicketService(controller, nextPageUrl));
                }
                return buildTicketFrame(response.getTicketList());
            } catch (IOException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(menuInput == Input.GET_TICKET) {
            controller.changeServiceState(new GetTicketService(controller));
            return MenuFrame.builder()
                    .header(Header.builder()
                            .appName(APP_NAME_VIEW)
                            .build())
                    .footer(Footer.builder()
                            .customMessage(ENTER_TICKET_ID)
                            .menu(MENU_VIEW)
                            .quit(QUIT_VIEW)
                            .build())
                    .build();
        }

        return  buildMenuFrameWithError();

    }

    private MenuFrame buildMenuFrameWithError() {
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(INVALID_INPUT)
                        .getAllTickets(GET_ALL_TICKET_VIEW)
                        .getTicket(GET_TICKET_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();
    }

    private GetAllTicketFrame buildTicketFrame(List<Ticket> ticketList) {

        Header header = Header.builder()
                .appName(APP_NAME_VIEW)
                .build();

        Footer footer = Footer.builder()
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

    private URI buildGetAllTicketURI() throws URISyntaxException {

        return new URIBuilder(BASE_URL + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(TICKET_PER_PAGE))
                .build();

    }

}
