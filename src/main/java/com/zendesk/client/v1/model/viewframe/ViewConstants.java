package com.zendesk.client.v1.model.viewframe;

import com.zendesk.client.v1.Input;

public final class ViewConstants {

    public static final String GREETING = String.format("%85s ", "*** Welcome ***\n");
    public static final String APPNAME = String.format("%92s ", "*** Zendesk Ticket Viewer ***\n");
    public static final String MENU = String.format("%5s %1s %1s ", "press", Input.MENU.getInput(), "for menu");

    public static final String GETALLTICKET = String.format("%5s %1s %1s ", "press", Input.GET_ALL_TICKETS.getInput(),
            "to checkout all ticket summary ");

    public static final String QUIT = String.format("%5s %1s %1s ", "press", Input.QUIT.getInput(), "to quit");

    public static final String GET_NEXT = String.format("%5s %1s %1s ", "press", Input.NEXT.getInput(), "for next set of tickets ");
}
