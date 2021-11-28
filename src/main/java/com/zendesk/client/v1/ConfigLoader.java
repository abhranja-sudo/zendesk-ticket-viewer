package com.zendesk.client.v1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.zendesk.client.v1.Path.CONFIG_PATH;

public class ConfigLoader {
    //Get Base url from properties file
    public static String BASE_URL;
    static {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {

            Properties prop = new Properties();
            prop.load(input);
            BASE_URL = prop.getProperty("base.url");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
