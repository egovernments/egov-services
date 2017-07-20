package org.egov.wcms.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class WcmsConnectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WcmsConnectionApplication.class, args);
	}
	
	
	 @Bean
	    public MappingJackson2HttpMessageConverter jacksonConverter() {
	        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	        final ObjectMapper mapper = new ObjectMapper();
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        // mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH));
	        converter.setObjectMapper(mapper);
	        return converter;
	    }
}
