package com.zendesk.client.v1.model.getallticketresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData {

    @JsonProperty("has_more")
    private boolean hasMore;

    @JsonProperty("before_cursor")
    private String beforeCursor;

    @JsonProperty("after_cursor")
    private String afterCursor;

    public boolean isHasMore() {
        return hasMore;
    }

    public String getBeforeCursor() {
        return beforeCursor;
    }

    public String getAfterCursor() {
        return afterCursor;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "hasMore=" + hasMore +
                ", beforeCursor='" + beforeCursor + '\'' +
                ", afterCursor='" + afterCursor + '\'' +
                '}';
    }
}
