package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.*;
import com.zendesk.client.v1.model.ticket.Ticket;
import com.zendesk.client.v1.model.viewframe.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class GetAllTicketService extends Service{

    private String url;
    private final TicketRetriever ticketRetriever;
    private final ObjectMapper objectMapper;

    public GetAllTicketService(Controller controller, String url) {
        super(controller);
        this.url = url;
        ticketRetriever = new TicketRetriever();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public Frame execute(String input) {
        Input input1 = Input.valueOfInput(input);
        if(input1 == Input.NEXT) {
            try {
                String body = ticketRetriever.retrieve(URI.create(url));
                GetAllTicketResponse response = objectMapper.readValue(body, GetAllTicketResponse.class);
                if(!response.getMetaData().isHasMore()) {
                    controller.changeServiceState(new MenuService(controller));
                    return buildAllTicketFrameForEndPage(response.getTicketList());
                }
                url = response.getLinks().getNext();
                return buildAllTicketFrame(response.getTicketList());

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        else if(input1 == Input.MENU) {
            controller.changeServiceState(
                    new MenuService(controller));
            return buildMenuFrame();
        }

        return buildMenuFrameWithError();
    }

    private MenuFrame buildMenuFrameWithError() {
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage("Invalid Input. Please try again ")
                        .getAllTickets(GET_NEXT_VIEW)
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
                .customMessage("End of Page. Select Menu options below to continue")
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
