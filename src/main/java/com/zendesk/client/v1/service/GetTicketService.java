package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.ZendeskResponseException;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getticketresponse.GetTicketResponse;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class GetTicketService extends Service {

    private static final String NO_INPUT = "No input. Please try entering again";
    private static final String RECORD_NOT_FOUND = "Record Not Found. Please try with another ticket ID";
    private static final String INVALID_INPUT = "Invalid input. Please try again entering ID in positive Integer format";
    private static final String UNKNOWN_ERROR = "Unknown error";

    private final TicketRetriever ticketRetriever;
    private final ObjectMapper objectMapper;

    public GetTicketService(Controller controller) {
        super(controller);
        ticketRetriever = new TicketRetriever();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public Frame execute(String input) {

        if(input.length() == 0) {
            return buildMenuFrameWithError(NO_INPUT);
        }

        else if(Input.getInput(input) == Input.MENU) {
            controller.changeServiceState(new MenuService(controller, new GetAllTicketService(controller),
                    new GetTicketService(controller)));
            return buildMenuFrame();
        }

        try {
            String body = ticketRetriever.retrieve(buildURI(input));
            GetTicketResponse response = objectMapper.readValue(body, GetTicketResponse.class);
            Ticket ticket = response.getTicket();
            if(ticket != null) {
                return buildTicketFrame(ticket);
            }

            return buildMenuFrameWithError(response.getMessage());

        }

        catch (ZendeskResponseException e) {
            if(e.getStatusCode() == 400) {
                return buildMenuFrameWithError(INVALID_INPUT);
            }
            else if(e.getStatusCode() == 404) {
                return buildMenuFrameWithError(RECORD_NOT_FOUND);
            }
        }

        catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return buildMenuFrameWithError(UNKNOWN_ERROR);
    }

    private URI buildURI(String id) throws URISyntaxException {
        return new URIBuilder(BASE_URL + API_VERSION + GET_TICKET + id)
                .build();
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

}
