package com.zendesk.client.v1.service;

import com.zendesk.client.v1.Input;
import com.zendesk.client.v1.controller.Controller;
import com.zendesk.client.v1.model.viewframe.Footer;
import com.zendesk.client.v1.model.viewframe.Frame;
import com.zendesk.client.v1.model.viewframe.Header;
import com.zendesk.client.v1.model.viewframe.MenuFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static com.zendesk.client.v1.model.viewframe.ViewConstants.*;
import static com.zendesk.client.v1.model.viewframe.ViewConstants.QUIT_VIEW;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private Controller controller;
    private GetAllTicketService getAllTicketService;
    private GetTicketService getTicketService;
    private MenuService menuService;


    @BeforeEach
    void setUp() {
        controller = new Controller();
        getAllTicketService = mock(GetAllTicketService.class);
        getTicketService = mock(GetTicketService.class);
        menuService = new MenuService(controller, getAllTicketService, getTicketService);
    }

    @Test
    void whenMenuServiceCalledWithGetAllTicketInput() {
        String input = Input.GET_ALL_TICKETS.getValue();

        //  Controller's Service instance should transition to Get All Ticket Service state
        menuService.execute(input);

        Assertions.assertEquals(controller.getServiceState().getClass(), getAllTicketService.getClass());

        //getAllTicketService.execute() should be called
        verify(getAllTicketService, times(1)).execute(input);
    }

    @Test
    void whenMenuServiceCalledWithGetTicketInput() {
        String input = Input.GET_TICKET.getValue();

        //  Controller's Service instance should transition from Menu Service state to Get Ticket Service state
        Assertions.assertEquals(controller.getServiceState().getClass(), menuService.getClass());
        Frame frame = menuService.execute(input);

        Assertions.assertEquals(controller.getServiceState().getClass(), getTicketService.getClass());

        // should return menu frame asking for input
        Assertions.assertEquals(frame.getClass(), MenuFrame.class);


        MenuFrame actualFrame = (MenuFrame)frame;

        MenuFrame expectedFrame = MenuFrame.builder()
                .header(Header.builder()
                        .appName(APP_NAME_VIEW)
                        .build())
                .footer(Footer.builder()
                        .customMessage(ENTER_TICKET_ID)
                        .menu(MENU_VIEW)
                        .quit(QUIT_VIEW)
                        .build())
                .build();

        assertThat(actualFrame)
                .usingRecursiveComparison()
                .isEqualTo(expectedFrame);

    }

    @Test
    void whenMenuServiceCalledWithInvalidInput() {

        List<String> validInput = List.of(Input.GET_ALL_TICKETS.getValue(), Input.GET_TICKET.getValue());
        List<String> invalidInput = new ArrayList<>();


        // add all invalid alphanumeric to invalid list and then test with each input
        for(char i = 'a'; i <='z'; i++ ) {
            String input = Character.toString(i);
            if(!validInput.contains(input))
                invalidInput.add(input);
        }

        for(int i = 0; i <= 9; i++ ) {
            String input = Integer.toString(i);
            if(!validInput.contains(input))
                invalidInput.add(input);
        }

        Frame expectedFrame = MenuFrame.builder()
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

        for(String input: invalidInput) {
            Frame actualFrame = menuService.execute(input);
            assertThat(actualFrame)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedFrame);
        }
    }
}