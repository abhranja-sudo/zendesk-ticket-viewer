package com.zendesk.client.v1;

import java.util.HashMap;
import java.util.Map;

public enum Input {
    GET_ALL_TICKETS("1"),
    NEXT("n"),
    MENU("m"),
    QUIT("q");


    public final String input;

    public String getInput() {
        return input;
    }

    Input(String input) {
        this.input = input;
    }

    private static final Map<String, Input> INPUT_MAPPER = new HashMap<>();

    static {
        for (Input e: values()) {
            INPUT_MAPPER.put(e.input, e);
        }
    }

    public static Input valueOfInput(String input) {
        return INPUT_MAPPER.get(input);
    }
}
