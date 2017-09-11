package org.egov.tradelicense;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description : This is initialization class for tl-tradelicense module
 * 
 * @author Pavan Kumar Kamma
 *
 */

@SpringBootApplication(scanBasePackages = "org.egov")
@EnableWebMvc
@Import({ TracerConfiguration.class })
public class TradeLicenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeLicenseApplication.class, args);

	}

	@Bean
	public ResponseInfoFactory responseInfoFactory() {
		return new ResponseInfoFactory();
	}

	/**
	 * This method will create rest template object
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Value("${app.timezone}")
	private String timeZone;

	@PostConstruct
	public void initialize() {
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
}