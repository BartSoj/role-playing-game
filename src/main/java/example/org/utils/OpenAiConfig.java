package example.org.utils;

import java.io.IOException;
import java.util.Properties;

public class OpenAiConfig {
    public static String getApiKey() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResource("config.properties").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String apiKey = properties.getProperty("OPENAI_API_KEY");
        if (apiKey == null) {
            throw new RuntimeException("OPENAI_API_KEY is not set in config.properties");
        }
        return apiKey;
    }
}
