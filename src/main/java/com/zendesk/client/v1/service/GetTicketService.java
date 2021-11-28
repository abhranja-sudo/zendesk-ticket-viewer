package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.zendesk.client.v1.Config.baseUrl;
import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

/**
 * GetTicketService is responsible for handling user inputs when user is currently in GetTicket(get one ticket) context.
 */
public class GetTicketService extends Service {

    private static final String NO_INPUT = "No input. Please try entering again";
    private static final String RECORD_NOT_FOUND = "Record Not Found. Please try with another ticket ID";
    private static final String INVALID_INPUT = "Invalid input. Please try again entering ID in positive Integer format";
    private static final String UNKNOWN_ERROR = "Unknown error";

    private final TicketRetriever ticketRetriever;
    private final ObjectMapper objectMapper;

    //Dependency Injection through constructor (instead of creating objects inside) for easily testable code
    public GetTicketService(Controller controller, TicketRetriever ticketRetriever,
                            ObjectMapper objectMapper) {
        super(controller);
        this.ticketRetriever = ticketRetriever;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param input received from the user.
     *              possible valid input:
     *              1.  MENU - switch to menu context
     *              2.  any valid integer - calls the ticketRetriever to get the tickets by ID
     *
     *              All other input should be treated as invalid
     */

    @Override
    public Frame execute(String input) {

        if(input.length() == 0) {
            return buildMenuFrameWithError(NO_INPUT);
        }

        else if(Input.getInput(input) == Input.MENU) {
            controller.changeServiceState(new MenuService(controller,
                    new GetAllTicketService(controller, ticketRetriever, objectMapper), this));
            return MenuFrame.getFrameForHome();
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
            return buildMenuFrameWithError(ZENDESK_ERROR);
        }

        catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        return buildMenuFrameWithError(UNKNOWN_ERROR);
    }

    private URI buildURI(String id) throws URISyntaxException {
        return new URIBuilder(baseUrl + API_VERSION + GET_TICKET + id)
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
}
