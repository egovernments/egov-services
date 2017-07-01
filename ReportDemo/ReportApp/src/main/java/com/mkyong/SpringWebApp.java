/*package com.mkyong;

import org.egov.domain.model.ReportMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootApplication
public class SpringWebApp {
	
	
	@Autowired
	ReportMetaData reportMetaData;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringWebApp.class, args);
	}
	public void run() throws Exception {
		System.out.println(reportMetaData);
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
}*/