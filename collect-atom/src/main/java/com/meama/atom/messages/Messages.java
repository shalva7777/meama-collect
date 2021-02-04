package com.meama.atom.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Messages {

    private static final Logger log = LoggerFactory.getLogger(Messages.class);

    private static Map<String, Properties> messages = new HashMap<>();

    private static String defaultLocale = "en";

    public static String get(String key) {
        return get(key, defaultLocale);
    }

    public static String get(String key, String language) {
        if (key != null) {
            if (language == null) {
                language = defaultLocale;
            }
            Properties properties = messages.get(language);
            if (properties == null) {
                properties = new Properties();
                try {
                    InputStream is = Messages.class.getResourceAsStream("/" + language + "_atom_messages.properties");
                    Reader bufferedReader = new InputStreamReader(is, "UTF-8");
                    properties.load(bufferedReader);
                    messages.put(language, properties);
                } catch (Exception ex) {
                    log.error("Error reading messages", ex);
                    return get(key);
                }
            }
            return properties.getProperty(key, key);
        }
        return null;
    }
}
