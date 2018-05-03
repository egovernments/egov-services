package org.egov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author ghanshyam.rawat
 * @version 1.1.0
 */

@SpringBootApplication
public class UserApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	
	/**
	 * returns an instance of ObjectMapper, which can be accessed by other
	 * components by auto wiring
	 * 
	 * DeserializationFeature for failing on unknown properties has been disabled on this bean
	 * 
	 * @return {@link ObjectMapper}
	 */
	@Bean
	@Primary
	public ObjectMapper getObjectMapper() {

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper;
	}
	
	/**
	 * Returns mapper with all the appropriate properties reqd in our functionalities.
	 * 
	 * @return ObjectMapper
	 */
	@Bean("searcher")
	public ObjectMapper getObjectMapperForSearcher() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return mapper;
	}
}
