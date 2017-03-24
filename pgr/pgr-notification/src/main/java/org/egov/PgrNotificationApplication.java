package org.egov;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SpringBootApplication
public class PgrNotificationApplication {

    private static final String TEMPLATE_TYPE = "txt";
    private static final String TEMPLATE_FOLDER = "templates";
    private static final int PRIORITY = 1;
    private static final String IST = "Asia/Calcutta";


    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        SpringApplication.run(PgrNotificationApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy hh:mm a"));
        return mapper;
    }

    @Bean
    public MustacheEngine getMustacheEngine() {
        ClassPathTemplateLocator classPathTemplateLocator =
            new ClassPathTemplateLocator(PRIORITY, TEMPLATE_FOLDER, TEMPLATE_TYPE);
        return MustacheEngineBuilder.newBuilder()
            .addTemplateLocator(classPathTemplateLocator)
            .build();
    }
}
