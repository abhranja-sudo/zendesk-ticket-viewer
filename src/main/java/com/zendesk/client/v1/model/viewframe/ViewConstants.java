package com.zendesk.client.v1.model.viewframe;

import com.zendesk.client.v1.Input;

public final class ViewConstants {

    public static final String GREETING_VIEW = String.format("%85s ", "*** Welcome ***\n");
    public static final String APP_NAME_VIEW = String.format("%92s ", "*** Zendesk Ticket Viewer ***\n");
    public static final String MENU_VIEW = String.format("%5s %1s %1s ", "press", Input.MENU.getInput(), "for menu");

    public static final String GET_ALL_TICKET_VIEW = String.format("%5s %1s %1s ", "press", Input.GET_ALL_TICKETS.getInput(),
            "to retrieve all ticket ");

    public static final String GET_TICKET_VIEW = String.format("%5s %1s %1s ", "press", Input.GET_TICKET.getInput(), "to retrieve a ticket");

    public static final String QUIT_VIEW = String.format("%5s %1s %1s ", "press", Input.QUIT.getInput(), "to quit");

    public static final String GET_NEXT_VIEW = String.format("%5s %1s %1s ", "press", Input.NEXT.getInput(), "for next set of tickets ");

    public static final String ENTER_TICKET_ID = "Enter Ticket ID : \n";

    public static final String INVALID_INPUT = "Invalid Input, Please try Again\n";

    public static final String END_OF_PAGE = String.format("%115s ",
            "********** End of Page. Select Menu options below to continue *********** ");


}
