package com.zendesk.client.v1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.zendesk.client.v1.Path.CONFIG_PATH;

/**
 * Get config from config.properties
 */
public class Config {

    public static String baseUrl;
    public static String bearerToken;

    static {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {

            Properties prop = new Properties();
            prop.load(input);
            baseUrl = prop.getProperty("base.url");
            bearerToken = prop.getProperty("jwt.token");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //check if baseurl ends with '/', if not add it.
        if(baseUrl.charAt(baseUrl.length() - 1) != '/') {
            baseUrl = baseUrl + '/';
        }
    }
}
