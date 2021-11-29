package com.zendesk.client.v1.view;

import com.zendesk.client.v1.model.viewframe.Frame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Responsible for displaying prompt and taking user input.
 */
public class Viewer {

    public String prompt(String prompt) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print(prompt);
            return br.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void display(Frame frame) {
        System.out.println(frame);
    }
}
