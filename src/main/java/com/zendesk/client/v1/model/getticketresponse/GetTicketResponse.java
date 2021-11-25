package com.zendesk.client.v1.model.getticketresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zendesk.client.v1.model.ticket.Ticket;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTicketResponse {

    @JsonProperty("ticket")
    Ticket ticket;

    @JsonProperty("description")
    String message;

    public Ticket getTicket() {
        return ticket;
    }

    public String getMessage() {
        return message;
    }

}
