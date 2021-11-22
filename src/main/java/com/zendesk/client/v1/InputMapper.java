package com.zendesk.client.v1;

import java.util.HashMap;
import java.util.Map;

public enum InputMapper {
    GET_ALL_TICKETS("1"),
    NEXT("n"),
    MENU("menu");


    public final String input;

    InputMapper(String input) {
        this.input = input;
    }

    private static final Map<String, InputMapper> INPUT_MAPPER_MAP = new HashMap<>();

    static {
        for (InputMapper e: values()) {
            INPUT_MAPPER_MAP.put(e.input, e);
        }
    }

    public static InputMapper valueOfInput(String input) {
        return INPUT_MAPPER_MAP.get(input);
    }
}
