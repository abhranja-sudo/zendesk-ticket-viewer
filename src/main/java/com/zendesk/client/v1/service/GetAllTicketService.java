package com.zendesk.client.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.getallticketresponse.Response;
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
    public Frame execute(Input input) {
        if(input == Input.NEXT) {
            try {
                String body = ticketRetriever.retrieve(URI.create(url));
                Response response = objectMapper.readValue(body, Response.class);
                if(!response.getMetaData().isHasMore()) {
                    controller.changeServiceState(
                            new MenuService(controller));
                }
                url = response.getLinks().getNext();
                return buildAllTicketFrame(response.getTicketList());

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        else if(input == Input.MENU) {
            controller.changeServiceState(
                    new MenuService(controller));
            return buildMenuFrame();
        }

        return buildMenuFrameWithError();
    }

    private MenuFrame buildMenuFrameWithError() {
        return MenuFrame.builder()
                .header(Header.builder()
                        .appName(APPNAME)
                        .build())
                .footer(Footer.builder()
                        .errorMessage("Invalid Input. Please try again ")
                        .allTickets(GET_NEXT)
                        .menu(MENU)
                        .quit(QUIT)
                        .build())
                .build();
    }

    private MenuFrame buildMenuFrame() {
        return MenuFrame.builder()
                .header(Header.builder()
                        .greeting(GREETING)
                        .appName(APPNAME)
                        .build())
                .footer(Footer.builder()
                        .allTickets(GETALLTICKET)
                        .quit(QUIT)
                        .build())
                .build();
    }

    private AllTicketFrame buildAllTicketFrame(List<Ticket> ticketList) {

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
}
