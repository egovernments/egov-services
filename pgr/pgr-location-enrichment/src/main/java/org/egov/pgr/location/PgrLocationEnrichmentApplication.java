package org.egov.pgr.location;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.pgr.location.json.ObjectMapperFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
@Import({ TracerConfiguration.class })
public class PgrLocationEnrichmentApplication {

	private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
		return ObjectMapperFactory.create();
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
		objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
		converter.setObjectMapper(objectMapper);
		return converter;
	}

	public static void main(String[] args) {
		SpringApplication.run(PgrLocationEnrichmentApplication.class, args);
	}
}
