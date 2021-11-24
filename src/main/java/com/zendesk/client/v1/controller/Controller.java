package com.zendesk.client.v1.controller;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.model.viewframe.Footer;
import com.zendesk.client.v1.model.viewframe.Frame;
import com.zendesk.client.v1.model.viewframe.Header;
import com.zendesk.client.v1.model.viewframe.MenuFrame;
import com.zendesk.client.v1.service.MenuService;
import com.zendesk.client.v1.service.Service;
import com.zendesk.client.v1.view.Viewer;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;

public class Controller {

    private final Viewer viewer;
    private Frame frame;
    private Service service;


    public Controller() {
        this.viewer = new Viewer();
        this.frame = buildMenuFrame();
        this.service = new MenuService(this);
    }

    public void changeServiceState(Service service) {
        this.service = service;
    }

    public void run() throws URISyntaxException, IOException, InterruptedException {
        viewer.display(frame);
        Input input;
        while ((input = Input.valueOfInput(viewer.prompt("\nTicketViewer> "))) != Input.QUIT) {
            Frame updatedFrame = service.execute(input);
            viewer.display(updatedFrame);
        }
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
}
