package com.zendesk.client.v1.model.getallticketresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zendesk.client.v1.model.ticket.Ticket;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("tickets")
    List<Ticket> ticketList;

    @JsonProperty("links")
    Links links;

    @JsonProperty("meta")
    MetaData metaData;

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public Links getLinks() {
        return links;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "ticketList=" + ticketList +
                ", links=" + links +
                ", metaData=" + metaData +
                '}';
    }

}
