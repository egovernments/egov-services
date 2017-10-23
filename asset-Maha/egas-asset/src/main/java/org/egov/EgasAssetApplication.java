package org.egov;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@Import({ TracerConfiguration.class })
public class EgasAssetApplication {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    public static void main(final String[] args) {
        SpringApplication.run(EgasAssetApplication.class, args);
    }

    @Bean
    public ObjectMapper getObjectMapper() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT,
        // Locale.ENGLISH));
        mapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        converter.setObjectMapper(mapper);
        return converter;
    }

   /* public AssetDepreciator getAssetDepreciator() {
        return new AssetDepreciatorImpl();
    }
    
    @Bean
    public CurrentValueRowMapper getCurrentValueRowMapper() {
        return new CurrentValueRowMapper();
    }*/

}