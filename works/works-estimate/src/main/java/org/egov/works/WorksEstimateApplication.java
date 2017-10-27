package org.egov.works;

import java.net.UnknownHostException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Import({ TracerConfiguration.class })
public class WorksEstimateApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorksEstimateApplication.class, args);
	}

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void init() throws UnknownHostException {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
		return objectMapper;
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		return converter;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
