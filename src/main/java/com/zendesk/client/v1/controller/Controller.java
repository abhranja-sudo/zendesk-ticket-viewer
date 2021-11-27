package com.zendesk.client.v1.controller;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.model.viewframe.Footer;
import com.zendesk.client.v1.model.viewframe.Frame;
import com.zendesk.client.v1.model.viewframe.Header;
import com.zendesk.client.v1.model.viewframe.MenuFrame;
import com.zendesk.client.v1.service.GetAllTicketService;
import com.zendesk.client.v1.service.GetTicketService;
import com.zendesk.client.v1.service.MenuService;
import com.zendesk.client.v1.service.Service;
import com.zendesk.client.v1.view.Viewer;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class Controller {

    private final Viewer viewer;
    private Frame frame;
    private Service service;
    private static final String QUIT = Input.QUIT.getValue();


    public Controller() {
        this.viewer = new Viewer();
        this.frame = buildMenuFrame();
        this.service = new MenuService(this, new GetAllTicketService(this), new GetTicketService(this));
    }

    public Service getService() {
        return service;
    }

    public void changeServiceState(Service service) {
        this.service = service;
    }

    public void run() {
        viewer.display(frame);

        String input;
        while (!QUIT.equals(input = viewer.prompt("\nEnter your Input here: > ").toLowerCase().trim())) {
            Frame updatedFrame = service.execute(input);
            viewer.display(updatedFrame);
        }
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
