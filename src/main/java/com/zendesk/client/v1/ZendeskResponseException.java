package com.zendesk.client.v1;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

public class ZendeskResponseException extends RuntimeException {

    private int statusCode;
    private String body;

    public ZendeskResponseException(int statusCode, String body) {
        super(MessageFormat.format("response code : {0} - {1}", statusCode, body));
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
