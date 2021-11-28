package com.zendesk.client.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.ZendeskResponseException;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.*;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.zendesk.client.v1.Config.baseUrl;
import static com.zendesk.client.v1.Path.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

/**
 *   GetAllTicketService is responsible for handling user inputs when user is currently in GetAllTicket context
 */
public class GetAllTicketService extends Service {

    private String url;
    private final TicketRetriever ticketRetriever;
    private final ObjectMapper objectMapper;
    private static final int TICKET_PER_PAGE = 25;

    //Dependency Injection through constructor (instead of creating objects inside) for easily testable code (TDD)
    public GetAllTicketService(Controller controller, TicketRetriever ticketRetriever,
                               ObjectMapper objectMapper) {
        super(controller);
        this.ticketRetriever = ticketRetriever;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param input received from the user.
     *              possible valid input:
     *              1.  GET_ALL_TICKETS - starts viewing the tickets from beginning
     *              2.  NEXT - starts viewing next page of tickets
     *              Note: If this is the last page of tickets, the context will switch to menu.
     *
     *              3.  MENU - switch to menu context
     *
     *              All other input should be treated as invalid
     */

    @Override
    public Frame execute(String input) {

        Input menuInput = Input.getInput(input);

        if(menuInput == Input.GET_ALL_TICKETS) {
            String body;
            try {
                body = ticketRetriever.retrieve(buildGetAllTicketURI());
                return processBody(body);

            } catch (ZendeskResponseException e) {
                return processZendeskException(e);
            }
            catch (IOException | InterruptedException | URISyntaxException e) {
                return processFatalException(FATAL_PROGRAM_ERROR);
            }
        }


        if(menuInput == Input.NEXT) {
            try {
                String body = ticketRetriever.retrieve(URI.create(url));
                return processBody(body);

            } catch (ZendeskResponseException e) {
                return processZendeskException(e);
            } catch (IOException | InterruptedException e) {
                return processFatalException(FATAL_PROGRAM_ERROR);
            }

        }

        else if(menuInput == Input.MENU) {
            changeStateToMenuService();
            return MenuFrame.getFrameForHome();
        }

        //For any other Invalid Input
        return buildMenuFrameWithError();
    }

    private Frame processBody(String body) throws JsonProcessingException {

        GetAllTicketResponse response = objectMapper.readValue(body, GetAllTicketResponse.class);
        if(!response.getMetaData().isHasMore()) {
            changeStateToMenuService();
            return buildAllTicketFrameForEndPage(response.getTicketList());
        }
        setUrlForFetchingNextPage(response);
        return buildAllTicketFrame(response.getTicketList());
    }

    private Frame processZendeskException(ZendeskResponseException e) {
        if(e.getStatusCode() == 401)
            return processFatalException(ZENDESK_INVALID_CRED);
        return processFatalException(ZENDESK_ERROR);
    }

    private void setUrlForFetchingNextPage(GetAllTicketResponse response) {
        url = response.getLinks().getNext();
    }

    private URI buildGetAllTicketURI() throws URISyntaxException {

        return new URIBuilder(baseUrl + API_VERSION + GET_ALL_TICKETS )
                .addParameter("page[size]", Integer.toString(TICKET_PER_PAGE))
                .build();
    }

    private void changeStateToMenuService() {
        controller.changeServiceState(new MenuService(controller, this,
                new GetTicketService(controller, ticketRetriever, objectMapper)));
    }

    private Frame processFatalException(String message) {
        changeStateToMenuService();
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(message)
                        .getAllTickets(GET_ALL_TICKET_VIEW)
                        .getTicket(GET_TICKET_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();
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



}
