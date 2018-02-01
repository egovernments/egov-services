package org.egov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Veswanth
 * 
 *  The LCMS (Legal Case Management System) includes the following key features. 
 *        
 *         ⦁  Summon/Warrant detail Entry
 *         ⦁  Assigning Advocate 
 *         ⦁  Case Registration 
 *         ⦁  Vakalatnama Generation 
 *         ⦁  Parawise comments Creation 
 *         ⦁  Hearing Process detail entry 
 *         ⦁  Opinion against the case.
 * 
 */
@SpringBootApplication
public class EgovLcmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(EgovLcmsApplication.class, args);
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
