package com.zendesk.client.v1.model.getallticketresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {

    @JsonProperty("prev")
    private String prev;

    @JsonProperty("next")
    private String next;

    public String getPrev() {
        return prev;
    }

    public String getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "Links{" +
                "prev='" + prev + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
