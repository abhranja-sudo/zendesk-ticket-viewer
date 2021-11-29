package com.zendesk.client.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.TicketRetriever;
import com.zendesk.client.v1.model.viewframe.Frame;
import com.zendesk.client.v1.model.viewframe.MenuFrame;
import com.zendesk.client.v1.service.GetAllTicketService;
import com.zendesk.client.v1.service.GetTicketService;
import com.zendesk.client.v1.service.MenuService;
import com.zendesk.client.v1.service.Service;
import com.zendesk.client.v1.view.Viewer;

/**
 * Controller stores a reference to one of the concrete service objects and delegates to it all state-specific work.
 * The controller communicates with the service object via the service abstract class. The controller exposes a changeServiceState()
 * for passing it a new state object.
 */
public class Controller {

    private final Viewer viewer;
    private Frame frame;
    private Service serviceState;
    private static final String QUIT = Input.QUIT.getValue();
    private TicketRetriever ticketRetriever;
    private ObjectMapper objectMapper;
    private GetAllTicketService getAllTicketService;
    private GetTicketService getTicketService;
    private MenuService menuService;


    public Controller() {
        this.viewer = new Viewer();
        this.ticketRetriever = new TicketRetriever();
        this.objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());
        this.frame = MenuFrame.getFrameForHome();
        this.getAllTicketService =  new GetAllTicketService(this, ticketRetriever, objectMapper);
        this.getTicketService =  new GetTicketService(this, ticketRetriever, objectMapper);
        this.menuService = new MenuService(this, getAllTicketService, getTicketService);
        this.serviceState = menuService;

    }

    public Service getServiceState() {
        return serviceState;
    }

    public void changeServiceState(Service service) {
        this.serviceState = service;
    }

    /**
     * Takes input from the viewer and delegates it to service to execute business logic
     */
    public void run() {
        viewer.display(frame);

        String input;
        while (!QUIT.equals(input = viewer.prompt("\nEnter your Input here: > ").toLowerCase().trim())) {
            Frame updatedFrame = serviceState.execute(input);
            viewer.display(updatedFrame);
        }
    }
}
