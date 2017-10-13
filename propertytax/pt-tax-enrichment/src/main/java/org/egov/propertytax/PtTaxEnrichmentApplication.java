package org.egov.propertytax;

import org.egov.models.ResponseInfoFactory;
import org.egov.propertytax.config.PropertiesManager;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Import({ TracerConfiguration.class })
@EnableWebMvc
public class PtTaxEnrichmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtTaxEnrichmentApplication.class, args);
	}

	@Bean
	public ResponseInfoFactory responseInfoFactory() {
		return new ResponseInfoFactory();
	}


	@Bean
	public PropertiesManager getPropertiesManager() {
		return new PropertiesManager();
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		converter.setObjectMapper(mapper);
		return converter;
	}
}
