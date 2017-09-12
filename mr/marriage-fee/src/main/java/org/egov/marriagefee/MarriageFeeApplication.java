package org.egov.marriagefee;

import org.egov.marriagefee.config.PropertiesManager;


import org.egov.marriagefee.web.contract.ResponseInfoFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Import({ TracerConfiguration.class })
public class MarriageFeeApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(MarriageFeeApplication.class, args);
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
