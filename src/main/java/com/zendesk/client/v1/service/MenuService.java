package com.zendesk.client.v1.service;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.viewframe.*;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class MenuService extends Service {

    private GetAllTicketService getAllTicketService;
    private GetTicketService getTicketService;

    public MenuService(Controller controller) {
        super(controller);
    }

    @Override
    public Frame execute(String input) {

        Input menuInput = Input.valueOfInput(input);

        if(menuInput == Input.GET_ALL_TICKETS) {

            getAllTicketService = new GetAllTicketService(controller);
            controller.changeServiceState(getAllTicketService);
            return getAllTicketService.execute(input);

        }

        if(menuInput == Input.GET_TICKET) {
            getTicketService = new GetTicketService(controller);
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
