package com.zendesk.client.v1.model.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Priority {

    @JsonProperty("urgent")
    URGENT,

    @JsonProperty("high")
    HIGH,

    @JsonProperty("normal")
    NORMAL,

    @JsonProperty("low")
    LOW
}
