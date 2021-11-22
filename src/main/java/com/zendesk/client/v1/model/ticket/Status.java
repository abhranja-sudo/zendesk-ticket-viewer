package com.zendesk.client.v1.model.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {

    @JsonProperty("new")
    NEW,

    @JsonProperty("open")
    OPEN,

    @JsonProperty("pending")
    PENDING,

    @JsonProperty("hold")
    HOLD,

    @JsonProperty("solved")
    SOLVED,

    @JsonProperty("closed")
    CLOSED
}
