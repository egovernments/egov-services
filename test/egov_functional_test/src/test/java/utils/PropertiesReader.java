package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private Properties prop = new Properties();

    public PropertiesReader() {
        try {
            String env = System.getProperty("env");
            System.out.println("ENV: " + env);

            if (null == env) env = "local";

            String propertiesFilePath = env + ".properties";
            InputStream inputStream;
            inputStream = getInputStream(propertiesFilePath);
            prop.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(String propertiesFilePath) {
        return this.getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
    }


    public String getUrl() {
        return prop.getProperty("url");
    }

    public String getWaitTime() {
        return prop.getProperty("waitTime");
    }

}

