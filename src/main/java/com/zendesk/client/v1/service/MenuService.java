package com.zendesk.client.v1.service;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.viewframe.*;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class MenuService extends Service {

    private GetAllTicketService getAllTicketService;
    private GetTicketService getTicketService;

    //Dependency Injection through constructor (instead of creating objects here) for easily testable code
    public MenuService(Controller controller, GetAllTicketService getAllTicketService,
                       GetTicketService getTicketService) {
        super(controller);
        this.getAllTicketService = getAllTicketService;
        this.getTicketService = getTicketService;
    }

    @Override
    public Frame execute(String input) {

        Input menuInput = Input.getInput(input);

        if(menuInput == Input.GET_ALL_TICKETS) {

            controller.changeServiceState(getAllTicketService);
            return getAllTicketService.execute(input);

        }

        if(menuInput == Input.GET_TICKET) {
            controller.changeServiceState(getTicketService);
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

}
